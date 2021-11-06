package com.whatshelp.ui.calllogs

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

    val hasPermission = MutableLiveData(false)

    val callLogs = MutableLiveData<List<CallLog>>(emptyList())

    internal fun loadCallLogs() {
        setPermission(true)
        viewModelScope.launch {
            callLogManager
                .getCallLogs()
                .let {
                    withContext(Dispatchers.Main) {
                        callLogs.value = it
                    }
                }
        }
    }

    fun setPermission(hasPermission: Boolean) {
        this.hasPermission.value = hasPermission
    }
}