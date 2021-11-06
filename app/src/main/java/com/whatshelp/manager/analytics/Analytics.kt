package com.whatshelp.manager.analytics

/**
 * Created by Vipin KT on 02/11/21
 */
object Analytics {

    object Event {
        const val RATE_APP = "rate_app"
        const val SHARE_APP = "share_app"
        const val THEME_APP = "theme_app"

        const val SHARE_LINK = "share_link"
        const val OPEN_WHATSAPP = "open_whatsapp"
        const val CHANGE_COUNTRY = "change_country"
        const val CHANGE_CHAT_APP = "change_chat_app"

        const val SHOW_COPIED_NUMBER = "display_copy_dialog"
        const val USE_COPIED_NUMBER = "use_copied_number"
        const val IGNORE_COPIED_NUMBER = "ignore_copied_number"

        const val OPEN_NUMBER_PICKER = "open_logs_and_history"

        const val SELECT_HISTORY = "select_history"
        const val CLEAR_HISTORY = "clear_history"
        const val DELETE_HISTORY = "delete_history"

        const val OPEN_QUICK_MESSAGE = "open_quick_message"
        const val SELECT_QUICK_MESSAGE = "select_quick_message"
        const val CLEAR_QUICK_MESSAGE = "clear_quick_messages"
        const val DELETE_QUICK_MESSAGE = "delete_quick_message"

        const val NEW_QUICK_MESSAGE = "new_quick_message"
        const val DISCARD_QUICK_MESSAGE = "discard_quick_message"
        const val SAVE_QUICK_MESSAGE = "save_quick_message"

        const val OPEN_FEEDBACK = "feedback"
        const val CHANGE_FEEDBACK_TYPE = "change_feedback_type"
        const val SUBMIT_FEEDBACK = "submit_feedback"

        const val OPEN_ABOUT = "about"
        const val CLICK_DEVELOPER_CONNECT = "click_developer_connect"

        const val APP_UPDATE_AVAILABLE = "app_update_available"
        const val APP_UPDATE_DOWNLOADED = "app_update_downloaded"

        const val CL_SELECT = "select_call_log"
        const val CL_OPEN_PERMISSION_SETTINGS = "open_call_log_permission_settings"
        const val CL_SHOW_PERMISSION_RATIONALE = "show_call_log_permission_rationale"
    }

    object Param {
        const val APP_TYPE = "app_type"
        const val COUNTRY_CODE = "country_code"
        const val COUNTRY_NAME = "country_name"
    }
}