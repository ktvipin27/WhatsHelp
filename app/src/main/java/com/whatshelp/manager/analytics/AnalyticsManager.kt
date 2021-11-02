package com.whatshelp.manager.analytics

/**
 * Created by Vipin KT on 02/11/21
 */
interface AnalyticsManager {
    fun logScreenView(screenName: String)
    fun logMainMenuClick(menu: String)
    fun shareChatLink()
    fun openWhatsapp(appType: String)
    fun changeCountry(code: String, countryName: String)
    fun changeChatApp()
    fun logHistoryClick()
    fun logQuickMessageClick()
    fun logCopiedNumberDialog(event: Analytics.CopiedNumber)
}