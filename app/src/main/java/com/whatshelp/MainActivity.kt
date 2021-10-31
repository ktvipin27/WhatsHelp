package com.whatshelp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.whatshelp.databinding.ActivityMainBinding
import com.whatshelp.util.ClipboardUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var clipboardUtil: ClipboardUtil

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        /*val topLevelDestinationIds = setOf(
            R.id.navigation_chat, R.id.navigation_dashboard, R.id.navigation_notifications
        )

        appBarConfiguration = AppBarConfiguration( topLevelDestinationIds )*/

        with(navHostFragment.navController) {
            appBarConfiguration = AppBarConfiguration( graph )
            setupActionBarWithNavController(this, appBarConfiguration)
            navView.setupWithNavController(this)
        }

        /*navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id !in topLevelDestinationIds) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }
        }*/
    }

    override fun onResume() {
        super.onResume()
        binding.root.post {
            mainViewModel.updateCopiedText(clipboardUtil.primaryClipText)
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration)
}