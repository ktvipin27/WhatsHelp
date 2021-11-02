package com.whatshelp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.whatshelp.R
import com.whatshelp.databinding.ActivityMainBinding
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.manager.analytics.AnalyticsManager
import com.whatshelp.util.ClipboardUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var clipboardUtil: ClipboardUtil

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    private val appUpdateListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            analyticsManager.logEvent(AnalyticsEvent.AppUpdate.AppUpdateDownloaded)
            appUpdateManager.completeUpdate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        with(navHostFragment.navController) {
            appBarConfiguration = AppBarConfiguration(graph)
            setupActionBarWithNavController(this, appBarConfiguration)
        }

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                ) {
                    appUpdateManager.registerListener(appUpdateListener)
                    analyticsManager.logEvent(AnalyticsEvent.AppUpdate.AppUpdateAvailable)
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo, AppUpdateType.FLEXIBLE, this, UPDATE_REQUEST_CODE
                    )
                }
            }
    }

    override fun onResume() {
        super.onResume()
        binding.root.post {
            mainViewModel.updateCopiedText(clipboardUtil.primaryClipText)
        }

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    analyticsManager.logEvent(AnalyticsEvent.AppUpdate.AppUpdateDownloaded)
                    appUpdateManager.completeUpdate()
                }
            }
    }

    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration)

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(appUpdateListener)
    }

    companion object {
        const val UPDATE_REQUEST_CODE = 1001
    }
}