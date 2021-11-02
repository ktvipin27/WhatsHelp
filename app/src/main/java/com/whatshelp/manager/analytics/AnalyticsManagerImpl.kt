package com.whatshelp.manager.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Vipin KT on 02/11/21
 */
@Singleton
class AnalyticsManagerImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsManager {

    private fun logEvent(name: String) = firebaseAnalytics.logEvent(name) {}

    private inline fun logEvent(name: String, crossinline builder: ParametersBuilder.() -> Unit) =
        firebaseAnalytics.logEvent(name, builder)

    override fun logScreenView(screenName: String) =
        logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }

    override fun logMainMenuClick(menu: String) = logEvent(Analytics.Event.CLICK_MAIN_MENU) {
        param(Analytics.Param.MENU, menu)
    }

    override fun shareChatLink() = logEvent(Analytics.Event.SHARE_LINK)

    override fun openWhatsapp(appType: String) = logEvent(Analytics.Event.OPEN_WHATSAPP) {
        param(Analytics.Param.APP_TYPE, appType)
    }

    override fun changeCountry(code: String, countryName: String) =
        logEvent(Analytics.Event.CHANGE_COUNTRY) {
            param(Analytics.Param.COUNTRY_CODE, code)
            param(Analytics.Param.COUNTRY_NAME, countryName)
        }

    override fun changeChatApp() = logEvent(Analytics.Event.CHANGE_CHAT_APP)

    override fun logHistoryClick() = logEvent(Analytics.Event.CLICK_HISTORY)

    override fun logQuickMessageClick() = logEvent(Analytics.Event.CLICK_QUICK_MESSAGE)

    override fun logCopiedNumberDialog(event: Analytics.CopiedNumber) = logEvent(event.event)

}