package com.whatshelp.manager.whatsapp

import com.whatshelp.data.model.App

/**
 * Created by Vipin KT on 02/11/21
 */
interface WhatsAppManager {
    fun openChat(mobile: String, message: String, app: App)
    fun shareLink(mobile: String, message: String)
    fun isWhatsAppInstalled(): Boolean
    fun isWhatsAppBusinessInstalled(): Boolean
}