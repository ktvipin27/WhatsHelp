package com.github.ktvipin27.whatshelp.data.repo

import com.github.ktvipin27.whatshelp.data.db.dao.HistoryDao
import com.github.ktvipin27.whatshelp.data.db.entity.History
import com.github.ktvipin27.whatshelp.data.model.WhatsAppNumber
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

/**
 * Created by Vipin KT on 15/10/21
 */
@ViewModelScoped
class WhatsHelpRepoImpl @Inject constructor(private val historyDao: HistoryDao) : WhatsHelpRepo {

    override suspend fun saveHistory(whatsAppNumber: WhatsAppNumber, formattedNumber: String) {
        val existing = historyDao.get(whatsAppNumber.code, whatsAppNumber.number)
        if (existing == null) {
            val history = History(
                whatsAppNumber = whatsAppNumber,
                formattedNumber = formattedNumber
            )
            historyDao.insert(history)
        } else
            historyDao.update(existing.apply { timeStamp = System.currentTimeMillis() })
    }

    override suspend fun getHistory(): List<History> {
        return historyDao.getAll()
    }
}