package com.github.ktvipin27.whatshelp.ui.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.ktvipin27.whatshelp.data.db.entity.Message
import com.github.ktvipin27.whatshelp.data.repo.WhatsHelpRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(private val whatsHelpRepo: WhatsHelpRepo) : ViewModel() {

    val messages: LiveData<List<Message>> = whatsHelpRepo.getMessages().asLiveData()

    fun onClickDeleteMessage(message: Message) {
        viewModelScope.launch {
            whatsHelpRepo.deleteMessage(message)
        }
    }

    fun addMessage(text:String){
        viewModelScope.launch {
            whatsHelpRepo.addMessage(text)
        }
    }
}