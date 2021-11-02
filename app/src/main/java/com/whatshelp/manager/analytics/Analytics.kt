package com.whatshelp.manager.analytics

import com.whatshelp.manager.analytics.Analytics.Event.IGNORE_COPIED_NUMBER
import com.whatshelp.manager.analytics.Analytics.Event.SHOW_COPIED_NUMBER
import com.whatshelp.manager.analytics.Analytics.Event.USE_COPIED_NUMBER

/**
 * Created by Vipin KT on 02/11/21
 */
object Analytics {

    object Event {
        const val CLICK_MAIN_MENU = "click_main_menu"

        const val SHARE_LINK = "share_link"
        const val OPEN_WHATSAPP = "open_whatsapp"
        const val CHANGE_COUNTRY = "change_country"
        const val CHANGE_CHAT_APP = "change_chat_app"

        const val SHOW_COPIED_NUMBER = "display_copy_dialog"
        const val USE_COPIED_NUMBER = "use_copied_number"
        const val IGNORE_COPIED_NUMBER = "ignore_copied_number"

        const val CLICK_HISTORY = "click_history_icon"
        const val SELECT_HISTORY = "select_history"
        const val CLEAR_HISTORY = "clear_history"
        const val DELETE_HISTORY = "delete_history"

        const val CLICK_QUICK_MESSAGE = "click_quick_message_icon"
        const val SELECT_QUICK_MESSAGE = "select_quick_message"
        const val CLEAR_QUICK_MESSAGE = "clear_quick_messages"
        const val DELETE_QUICK_MESSAGE = "delete_quick_message"
        const val ADD_QUICK_MESSAGE = "add_quick_message"
        const val DISCARD_QUICK_MESSAGE = "discard_quick_message"
        const val SAVE_QUICK_MESSAGE = "save_quick_message"

        const val CHANGE_FEEDBACK_TYPE = "change_feedback_type"
        const val SUBMIT_FEEDBACK = "submit_feedback"

        const val CLICK_DEVELOPER_CONNECT = "click_developer_connect"
    }

    object Param {

        const val APP_TYPE = "app_type"
        const val COUNTRY_CODE = "country_code"
        const val COUNTRY_NAME = "country_name"
        const val MENU = "menu"

        object Menu {
            const val SHARE_APP = "share_app"
            const val RATE_APP = "rate_app"
            const val FEEDBACK = "feedback"
            const val ABOUT = "about"
        }

    }


    sealed class CopiedNumber(val event: String) {
        object Display : CopiedNumber(SHOW_COPIED_NUMBER)
        object Ignore : CopiedNumber(USE_COPIED_NUMBER)
        object Use : CopiedNumber(IGNORE_COPIED_NUMBER)
    }
}