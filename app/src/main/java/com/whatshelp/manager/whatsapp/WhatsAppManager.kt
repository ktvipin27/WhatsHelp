package com.whatshelp.manager.whatsapp

/**
 * Created by Vipin KT on 02/11/21
 */
interface WhatsAppManager {
    fun openChat(mobile: String, message: String, packageName: String)
    fun shareLink(mobile: String, message: String)
    fun appInstalledOrNot(packageName: String): Boolean
    fun isWhatsAppInstalled(): Boolean
    fun isWhatsAppBusinessInstalled(): Boolean
}