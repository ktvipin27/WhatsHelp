package com.whatshelp.manager.calllog

import com.whatshelp.data.model.CallLog

/**
 * Created by Vipin KT on 05/11/21
 */
interface CallLogManager {
    suspend fun getCallLogs(): List<CallLog>
}