package com.whatshelp.manager.whatsapp

import android.content.Context
import com.whatshelp.R
import com.whatshelp.data.model.App
import com.whatshelp.data.model.WhatsApp
import com.whatshelp.data.model.WhatsAppBusiness
import com.whatshelp.manager.app.AppManager
import com.whatshelp.util.toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 14/10/21
 */
@Singleton
class WhatsAppManagerImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val appManager: AppManager,
) : WhatsAppManager {

    override fun openChat(mobile: String, message: String, app: App) {
        if (appManager.isAppInstalled(app.packageName)) {
            val chatLink = getChatLink(mobile, message)
            appManager.openApplication(chatLink, app.packageName)
        } else context.toast(context.getString(R.string.error_no_whatsapp, app.name))
    }

    override fun shareLink(mobile: String, message: String) {
        val chatLink = getChatLink(mobile, message)
        appManager.shareText(chatLink)
    }

    private fun getChatLink(mobile: String, message: String): String =
        StringBuilder("https://api.whatsapp.com/send").apply {
            if (mobile.isNotEmpty() || message.isNotEmpty())
                append("?")
            if (mobile.isNotEmpty() && message.isNotEmpty())
                append("phone=$mobile&text=$message")
            else if (mobile.isNotEmpty())
                append("phone=$mobile")
            else if (message.isNotEmpty())
                append("text=$message")

        }.toString().replace(" ", "%20")

    override fun isWhatsAppInstalled() = appManager.isAppInstalled(WhatsApp.packageName)

    override fun isWhatsAppBusinessInstalled() =
        appManager.isAppInstalled(WhatsAppBusiness.packageName)
}