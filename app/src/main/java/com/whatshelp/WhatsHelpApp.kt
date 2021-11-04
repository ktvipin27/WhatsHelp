package com.whatshelp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.whatshelp.manager.theme.ThemeManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Created by Vipin KT on 14/10/21
 */
@HiltAndroidApp
class WhatsHelpApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate() {
        super.onCreate()
        themeManager.setTheme()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}