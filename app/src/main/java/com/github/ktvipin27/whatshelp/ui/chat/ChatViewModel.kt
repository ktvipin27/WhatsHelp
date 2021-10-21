package com.github.ktvipin27.whatshelp.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ktvipin27.whatshelp.data.repo.WhatsHelpRepo
import com.github.ktvipin27.whatshelp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val whatsHelpRepo: WhatsHelpRepo) : ViewModel() {

    val state = SingleLiveEvent<ChatState>()

    val message = MutableLiveData("")

    fun onClickSend(
        isValidNumber: Boolean,
        countryCode: String,
        fullNumber: String,
        formattedFullNumber: String,
    ) {
        val msg = message.value.toString()
        val number = fullNumber.substring(countryCode.length, fullNumber.length)

        when {
            isValidNumber -> {
                saveHistory(formattedFullNumber, countryCode)

                state.value =
                    ChatState.OpenWhatsApp(fullNumber, msg)
            }
            number.isEmpty() -> {
                state.value = if (msg.isNotEmpty())
                    ChatState.OpenWhatsApp("", msg)
                else
                    ChatState.InvalidData
            }
            else -> {
                state.value = ChatState.InvalidNumber
            }
        }
    }

    private fun saveHistory(formattedFullNumber: String, countryCode: String) {
        viewModelScope.launch {
            whatsHelpRepo.saveHistory(countryCode, formattedFullNumber)
        }
    }

}