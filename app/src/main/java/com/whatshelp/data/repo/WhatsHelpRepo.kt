package com.whatshelp.data.repo

import com.whatshelp.data.db.entity.History
import com.whatshelp.data.db.entity.Message
import com.whatshelp.data.model.WhatsAppNumber
import kotlinx.coroutines.flow.Flow

/**
 * Created by Vipin KT on 15/10/21
 */
interface WhatsHelpRepo {

    suspend fun saveHistory(whatsAppNumber: WhatsAppNumber, formattedNumber: String)

    fun getHistory(): Flow<List<History>>

    suspend fun deleteHistory(history: History)

    fun getMessages(): Flow<List<Message>>

    suspend fun deleteMessage(message: Message)

    suspend fun addMessage(text: String)
}