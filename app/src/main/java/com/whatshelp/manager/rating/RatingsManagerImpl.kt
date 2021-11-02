package com.whatshelp.manager.rating

import android.content.ActivityNotFoundException
import android.content.Context
import com.whatshelp.BuildConfig
import com.whatshelp.manager.app.AppManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 02/11/21
 */
@Singleton
class RatingsManagerImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val appManager: AppManager,
) : RatingsManager {

    private val urlMarket = "market://details?id=${BuildConfig.APPLICATION_ID}"
    private val urlMarketWeb =
        "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"

    override fun rateApp() {
        try {
            appManager.openApplication(urlMarket)
        } catch (e: ActivityNotFoundException) {
            appManager.openApplication(urlMarketWeb)
        }
    }
}