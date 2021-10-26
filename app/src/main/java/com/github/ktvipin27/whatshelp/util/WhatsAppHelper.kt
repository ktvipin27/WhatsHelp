package com.github.ktvipin27.whatshelp.util

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ShareCompat
import com.github.ktvipin27.whatshelp.util.Constants.PACKAGE_WHATSAPP
import com.github.ktvipin27.whatshelp.util.Constants.PACKAGE_WHATSAPP_BUSINESS
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * Created by Vipin KT on 14/10/21
 */
@ActivityScoped
class WhatsAppHelper @Inject constructor(@ApplicationContext val context: Context) {

    fun openChat(mobile: String, message: String) {

        val chatLink = getChatLink(mobile,message)

        val uri = Uri.parse(chatLink)
        val sendIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage(PACKAGE_WHATSAPP)
            flags = FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(sendIntent)
    }

    fun shareLink(mobile: String, message: String){
        val chatLink = getChatLink(mobile, message)
        val shareIntent = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setChooserTitle("Share Link")
            .setText(chatLink)
            .createChooserIntent()
            .apply { flags = FLAG_ACTIVITY_NEW_TASK }
        context.startActivity(shareIntent)
    }

    private fun getChatLink(mobile: String,message: String): String =
        "https://api.whatsapp.com/send?phone=$mobile&text=$message"

    fun isWhatsAppInstalled() = appInstalledOrNot(PACKAGE_WHATSAPP)

    fun isWhatsAppBusinessInstalled() = appInstalledOrNot(PACKAGE_WHATSAPP_BUSINESS)

    private fun appInstalledOrNot(packageName: String): Boolean = try {
        context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}