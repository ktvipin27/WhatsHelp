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

    val message = MutableLiveData("")

    fun onClickSend(countryCode:String,fullNumber: String,formattedFullNumber: String) {
        val msg = message.value.toString()

        if (fullNumber.isNotEmpty()) saveHistory(formattedFullNumber,countryCode)

        state.value =
            ChatState.OpenWhatsApp(fullNumber, msg)
    }

    private fun saveHistory(formattedFullNumber: String, countryCode: String) {
        viewModelScope.launch {
            whatsHelpRepo.saveHistory(countryCode, formattedFullNumber)
        }
    }

}