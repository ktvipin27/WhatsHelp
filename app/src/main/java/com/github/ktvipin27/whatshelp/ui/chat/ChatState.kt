package com.github.ktvipin27.whatshelp.ui.chat

import com.github.ktvipin27.whatshelp.data.model.App

/**
 * Created by Vipin KT on 15/10/21
 */
sealed class ChatState{
    data class OpenChat(val number:String, val message:String, val app: App): ChatState()
    data class ShareLink(val number:String, val message:String): ChatState()
    object InvalidNumber : ChatState()
    object InvalidData : ChatState()
}
