package com.whatshelp.data.repo

import com.whatshelp.data.db.dao.HistoryDao
import com.whatshelp.data.db.entity.History
import com.whatshelp.data.model.WhatsAppNumber
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

/**
 * Created by Vipin KT on 15/10/21
 */
@ViewModelScoped
class WhatsHelpRepoImpl @Inject constructor(private val historyDao: HistoryDao) : WhatsHelpRepo {

    override suspend fun saveHistory(whatsAppNumber: WhatsAppNumber, formattedNumber: String) {
        historyDao.delete(whatsAppNumber.code, whatsAppNumber.number)
        val history = History(
            whatsAppNumber = whatsAppNumber,
            formattedNumber = formattedNumber
        )
        historyDao.insert(history)
    }

    override suspend fun getHistory(): List<History> {
        return historyDao.getAll()
    }
}