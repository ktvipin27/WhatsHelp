package com.github.ktvipin27.whatshelp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.ktvipin27.whatshelp.databinding.ActivityMainBinding
import com.github.ktvipin27.whatshelp.util.ClipboardUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var clipboardUtil: ClipboardUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val topLevelDestinationIds = setOf(
            R.id.navigation_chat, R.id.navigation_dashboard, R.id.navigation_notifications
        )
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds
        )

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id !in topLevelDestinationIds) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.post {
            mainViewModel.updateCopiedText(clipboardUtil.primaryClipText)
        }
    }
}