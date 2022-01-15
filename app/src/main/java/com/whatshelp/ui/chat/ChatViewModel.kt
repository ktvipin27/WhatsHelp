package com.whatshelp.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whatshelp.data.model.App
import com.whatshelp.data.model.WhatsApp
import com.whatshelp.data.model.WhatsAppBusiness
import com.whatshelp.data.model.WhatsAppNumber
import com.whatshelp.data.repo.WhatsHelpRepo
import com.whatshelp.util.NumberUtil
import com.whatshelp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val numberUtil: NumberUtil, private val whatsHelpRepo: WhatsHelpRepo
) : ViewModel() {

    val state = SingleLiveEvent<ChatState>()
    val mobile = MutableStateFlow("")
    val message = MutableStateFlow("")
    val countryCode = MutableStateFlow("91")
    val countryNameCode = MutableStateFlow("IN")
    val supportedApps = arrayListOf(WhatsApp, WhatsAppBusiness)
    val selectedAppIndex = MutableStateFlow(0)

    fun onClickAction(
        action: ChatAction
    ) {
        val fullNumber = countryCode.value + mobile.value
        val selectedApp: App = supportedApps[selectedAppIndex.value]

        when {
            numberUtil.isValidFullNumber(mobile.value, countryNameCode.value) -> {
                numberUtil.getFormattedNumber(mobile.value, countryNameCode.value)
                    ?.let { saveHistory(countryCode.value, mobile.value, it) }

                if (action == ChatAction.OPEN_CHAT)
                    state.value =
                        ChatState.OpenChat(fullNumber, message.value, selectedApp)
                else
                    state.value =
                        ChatState.ShareLink(fullNumber, message.value)
            }
            mobile.value.isEmpty() -> {
                state.value = if (message.value.isNotEmpty()) {
                    if (action == ChatAction.OPEN_CHAT)
                        ChatState.OpenChat("", message.value, selectedApp)
                    else
                        ChatState.ShareLink("", message.value)
                } else
                    ChatState.InvalidData
            }
            else -> {
                state.value = ChatState.InvalidNumber
            }
        }
    }

    private fun saveHistory(
        countryCode: String,
        number: String,
        formattedNumber: String,
    ) {
        viewModelScope.launch {
            whatsHelpRepo.saveHistory(WhatsAppNumber(countryCode.toInt(), number), formattedNumber)
        }
    }

    fun setSelectedAppIndex(index: Int) {
        selectedAppIndex.value = index
    }

    fun setFullNumber(number: String, code: String) {
        //countryCode.value = code
        mobile.value = number
    }

}