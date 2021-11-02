package com.whatshelp.manager.analytics

/**
 * Created by Vipin KT on 02/11/21
 */
sealed class AnalyticsEvent(
    open val event: String,
    val params: List<Pair<String, String>> = emptyList(),
) {

    data class ChangeCountry(val code: String, val countryName: String) :
        AnalyticsEvent(Analytics.Event.CHANGE_COUNTRY,
            listOf(Analytics.Param.COUNTRY_CODE to code,
                Analytics.Param.COUNTRY_NAME to countryName))

    object ChangeChatApp : AnalyticsEvent(Analytics.Event.CHANGE_CHAT_APP)

    object ShareChatLink : AnalyticsEvent(Analytics.Event.SHARE_LINK)

    data class OpenWhatsApp(val appType: String) : AnalyticsEvent(Analytics.Event.OPEN_WHATSAPP,
        listOf(Analytics.Param.APP_TYPE to appType))

    sealed class CopiedNumber(override val event: String) : AnalyticsEvent(event) {
        object Display : CopiedNumber(Analytics.Event.SHOW_COPIED_NUMBER)
        object Ignore : CopiedNumber(Analytics.Event.USE_COPIED_NUMBER)
        object Use : CopiedNumber(Analytics.Event.IGNORE_COPIED_NUMBER)
    }

    sealed class History(override val event: String) : AnalyticsEvent(event) {
        object Open : History(Analytics.Event.OPEN_HISTORY)
        object Delete : History(Analytics.Event.DELETE_HISTORY)
        object Clear : History(Analytics.Event.CLEAR_HISTORY)
        object Click : History(Analytics.Event.SELECT_HISTORY)
    }

    sealed class QuickMessage(override val event: String) : AnalyticsEvent(event) {
        object Open : QuickMessage(Analytics.Event.OPEN_QUICK_MESSAGE)
        object Delete : QuickMessage(Analytics.Event.DELETE_QUICK_MESSAGE)
        object Clear : QuickMessage(Analytics.Event.CLEAR_QUICK_MESSAGE)
        object Click : QuickMessage(Analytics.Event.SELECT_QUICK_MESSAGE)

        object New : QuickMessage(Analytics.Event.NEW_QUICK_MESSAGE)
        object Discard : QuickMessage(Analytics.Event.DISCARD_QUICK_MESSAGE)
        object Save : QuickMessage(Analytics.Event.SAVE_QUICK_MESSAGE)
    }

    sealed class Feedback(override val event: String) : AnalyticsEvent(event) {
        object Open : QuickMessage(Analytics.Event.OPEN_FEEDBACK)
        object Change : QuickMessage(Analytics.Event.CHANGE_FEEDBACK_TYPE)
        object Submit : QuickMessage(Analytics.Event.SUBMIT_FEEDBACK)
    }

    object RateApp : AnalyticsEvent(Analytics.Event.RATE_APP)

    object ShareApp : AnalyticsEvent(Analytics.Event.SHARE_APP)

    sealed class About(override val event: String) : AnalyticsEvent(event) {
        object Open : About(Analytics.Event.OPEN_ABOUT)
        object DeveloperConnect : About(Analytics.Event.CLICK_DEVELOPER_CONNECT)
    }
}
