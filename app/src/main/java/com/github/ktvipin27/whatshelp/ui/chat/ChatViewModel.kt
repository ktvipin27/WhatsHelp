package com.github.ktvipin27.whatshelp.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ktvipin27.whatshelp.data.db.entity.History
import com.github.ktvipin27.whatshelp.data.repo.WhatsHelpRepo
import com.github.ktvipin27.whatshelp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val whatsHelpRepo: WhatsHelpRepo) : ViewModel() {

    val state = SingleLiveEvent<ChatState>()

    val mobileNumber = MutableLiveData("")
    val message = MutableLiveData("")

    fun onClickSend() {
        val number = mobileNumber.value.toString()
        val msg = message.value.toString()

        if (number.isNotEmpty())
            viewModelScope.launch {
                whatsHelpRepo.saveHistory(number)
            }

        state.value =
            ChatState.OpenWhatsApp(number, msg)
    }

    fun onReceiveHistory(history: History?) {
        history?.let {
            mobileNumber.value = it.number
        }
    }

}