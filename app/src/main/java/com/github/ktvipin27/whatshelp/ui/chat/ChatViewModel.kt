package com.github.ktvipin27.whatshelp.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ktvipin27.whatshelp.data.model.WhatsAppNumber
import com.github.ktvipin27.whatshelp.data.repo.WhatsHelpRepo
import com.github.ktvipin27.whatshelp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val whatsHelpRepo: WhatsHelpRepo) : ViewModel() {

    val state = SingleLiveEvent<ChatState>()

    val message = MutableLiveData("")

    fun onClickAction(
        action: ChatAction,
        isValidNumber: Boolean,
        countryCode: String,
        fullNumber: String,
        formattedNumber: String,
    ) {
        val msg = message.value.toString()
        val number = fullNumber.substring(countryCode.length, fullNumber.length)

        when {
            isValidNumber -> {
                saveHistory(countryCode, number, formattedNumber)

                if (action==ChatAction.OPEN_CHAT)
                state.value =
                    ChatState.OpenWhatsApp(fullNumber, msg)
                else
                state.value =
                    ChatState.ShareLink(fullNumber, msg)
            }
            number.isEmpty() -> {
                state.value = if (msg.isNotEmpty()) {
                    if (action == ChatAction.OPEN_CHAT)
                        ChatState.OpenWhatsApp("", msg)
                    else
                        ChatState.ShareLink("", msg)
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

}