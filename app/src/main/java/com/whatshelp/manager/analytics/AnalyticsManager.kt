package com.whatshelp.manager.analytics

/**
 * Created by Vipin KT on 02/11/21
 */
interface AnalyticsManager {
    fun logScreenView(screenName: String)
    fun logEvent(event: AnalyticsEvent)
}