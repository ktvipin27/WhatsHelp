package com.github.ktvipin27.whatshelp.data.repo

import com.github.ktvipin27.whatshelp.data.db.entity.History
import com.github.ktvipin27.whatshelp.data.db.entity.Message
import com.github.ktvipin27.whatshelp.data.model.WhatsAppNumber
import kotlinx.coroutines.flow.Flow

/**
 * Created by Vipin KT on 15/10/21
 */
interface WhatsHelpRepo {

    suspend fun saveHistory(whatsAppNumber: WhatsAppNumber, formattedNumber: String)

    suspend fun getHistory(): List<History>

    fun getMessages(): Flow<List<Message>>
}