package com.github.ktvipin27.whatshelp.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ktvipin27.whatshelp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    val state = SingleLiveEvent<ChatState>()

    val mobileNumber = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun onClickSend() {
        state.value =
            ChatState.OpenWhatsApp(mobileNumber.value.toString(), message.value.toString())
    }

}