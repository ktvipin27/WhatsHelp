package com.github.ktvipin27.whatshelp.util

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.net.Uri

/**
 * Created by Vipin KT on 14/10/21
 */
class WhatsAppHelper(private val context: Context) {

    fun sendMessage(mobile:String,message:String){
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=$message")
        val sendIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.whatsapp")
            flags = FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(sendIntent)
    }

    fun isWhatsAppInstalled() = appInstalledOrNot("com.whatsapp")

    fun isWhatsAppBusinessInstalled() = appInstalledOrNot("com.whatsapp.w4b")

    private fun appInstalledOrNot(packageName: String): Boolean = try {
        context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}