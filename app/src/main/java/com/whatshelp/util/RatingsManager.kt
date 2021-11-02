package com.whatshelp.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 02/11/21
 */
@Singleton
class RatingsManager @Inject constructor(@ApplicationContext val context: Context) {

    private val urlMarket = "market://details?id=${Constants.PACKAGE_WHATSHELP}"
    private val urlMarketWeb =
        "https://play.google.com/store/apps/details?id=${Constants.PACKAGE_WHATSHELP}"

    fun rateApp() {
        try {
            openView(urlMarket)
        } catch (e: ActivityNotFoundException) {
            openView(urlMarketWeb)
        }
    }

    private fun openView(url: String) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                .apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
        )
    }
}