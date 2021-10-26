package com.github.ktvipin27.whatshelp.ui.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.ktvipin27.whatshelp.data.db.entity.Message
import com.github.ktvipin27.whatshelp.data.repo.WhatsHelpRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(whatsHelpRepo: WhatsHelpRepo) : ViewModel() {

    val messages: LiveData<List<Message>> = whatsHelpRepo.getMessages().asLiveData()
}