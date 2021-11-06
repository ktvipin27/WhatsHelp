package com.whatshelp.ui.calllogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whatshelp.data.model.CallLog
import com.whatshelp.manager.calllog.CallLogManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CallLogsViewModel @Inject constructor(private val callLogManager: CallLogManager) :
    ViewModel() {

    private val _showPermissionCard = MutableLiveData(false)
    val showPermissionCard: LiveData<Boolean>
        get() = _showPermissionCard

    private val _callLogs = MutableLiveData<List<CallLog>>(emptyList())
    val callLogs: LiveData<List<CallLog>>
        get() = _callLogs

    private fun loadCallLogs() {
        viewModelScope.launch {
            callLogManager
                .getCallLogs()
                .let {
                    withContext(Dispatchers.Main) {
                        _callLogs.value = it
                    }
                }
        }
    }

    fun setPermissionGranted(isGranted: Boolean) {
        this._showPermissionCard.value = !isGranted
        if (isGranted) loadCallLogs()
        else _callLogs.value = emptyList()
    }
}