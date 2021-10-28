package com.whatshelp.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.whatshelp.data.db.entity.History
import com.whatshelp.data.repo.WhatsHelpRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val whatsHelpRepo: WhatsHelpRepo) : ViewModel() {

    val history: LiveData<List<History>> = liveData {
        emit(whatsHelpRepo.getHistory())
    }
}