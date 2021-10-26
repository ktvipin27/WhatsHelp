package com.github.ktvipin27.whatshelp.data.repo

import com.github.ktvipin27.whatshelp.data.db.dao.HistoryDao
import com.github.ktvipin27.whatshelp.data.db.dao.MessageDao
import com.github.ktvipin27.whatshelp.data.db.entity.History
import com.github.ktvipin27.whatshelp.data.db.entity.Message
import com.github.ktvipin27.whatshelp.data.model.WhatsAppNumber
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Vipin KT on 15/10/21
 */
@ViewModelScoped
class WhatsHelpRepoImpl @Inject constructor(private val historyDao: HistoryDao,private val messageDao: MessageDao) : WhatsHelpRepo {

    override suspend fun saveHistory(whatsAppNumber: WhatsAppNumber, formattedNumber: String) {
        historyDao.delete(whatsAppNumber.code, whatsAppNumber.number)
        val history = History(
            whatsAppNumber = whatsAppNumber,
            formattedNumber = formattedNumber
        )
        historyDao.insert(history)
    }

    override suspend fun getHistory(): List<History> = historyDao.getAll()

    override fun getMessages(): Flow<List<Message>> = messageDao.getAll()
}