package com.whatshelp.ui.history

import androidx.lifecycle.*
import com.whatshelp.data.db.entity.History
import com.whatshelp.data.repo.WhatsHelpRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val whatsHelpRepo: WhatsHelpRepo) : ViewModel() {

    val history: LiveData<List<History>> = whatsHelpRepo.getHistory().asLiveData()

    fun deleteHistory(position:Int) {
        viewModelScope.launch {
            history.value?.get(position)?.let { whatsHelpRepo.deleteHistory(it) }
        }
    }
}