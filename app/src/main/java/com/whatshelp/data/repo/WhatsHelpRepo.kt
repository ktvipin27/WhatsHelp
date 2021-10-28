package com.whatshelp.data.repo

import com.whatshelp.data.db.entity.History
import com.whatshelp.data.model.WhatsAppNumber

/**
 * Created by Vipin KT on 15/10/21
 */
interface WhatsHelpRepo {

    suspend fun saveHistory(whatsAppNumber: WhatsAppNumber, formattedNumber: String)

    suspend fun getHistory(): List<History>
}