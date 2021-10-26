package com.github.ktvipin27.whatshelp.ui.chat

/**
 * Created by Vipin KT on 15/10/21
 */
sealed class ChatState{
    data class OpenWhatsApp(val number:String, val message:String): ChatState()
    data class ShareLink(val number:String, val message:String): ChatState()
    object InvalidNumber : ChatState()
    object InvalidData : ChatState()
}
