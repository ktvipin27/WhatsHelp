package com.whatshelp.manager.calllog

import android.annotation.SuppressLint
import android.content.Context
import com.whatshelp.data.model.CallLog
import com.whatshelp.util.getDateAgo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 05/11/21
 */
@Singleton
class CallLogManagerImpl @Inject constructor(@ApplicationContext val context: Context) :
    CallLogManager {

    @SuppressLint("Range")
    override suspend fun getCallLogs(): List<CallLog> {
        return withContext(Dispatchers.IO) {
            val strOrder = android.provider.CallLog.Calls.DATE + " DESC"
            val callUri = android.provider.CallLog.Calls.CONTENT_URI
            val projection = arrayOf(
                android.provider.CallLog.Calls.NUMBER,
                android.provider.CallLog.Calls.CACHED_NAME,
                android.provider.CallLog.Calls.DATE,
                android.provider.CallLog.Calls.TYPE
            )

            val callLogList = mutableListOf<CallLog>()
            context.contentResolver.query(callUri,
                projection,
                null,
                null,
                strOrder)
                ?.let { cursor ->
                    cursor.moveToFirst()
                    while (!cursor.isAfterLast) {

                        val number =
                            cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER))
                        val name =
                            cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME))
                        val millisDate =
                            cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE))
                        val callType =
                            cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE))

                        val date = millisDate.toLong().getDateAgo()

                        callLogList.add(CallLog(number, name ?: "<unknown>", date, callType))
                        cursor.moveToNext()
                    }
                    cursor.close()
                }
            callLogList.toList()
        }

    }

}