package com.whatshelp.data.repo

import com.whatshelp.data.db.dao.HistoryDao
import com.whatshelp.data.db.dao.MessageDao
import com.whatshelp.data.db.entity.History
import com.whatshelp.data.db.entity.Message
import com.whatshelp.data.model.WhatsAppNumber
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Vipin KT on 15/10/21
 */
@ViewModelScoped
class WhatsHelpRepoImpl @Inject constructor(
    private val historyDao: HistoryDao,
    private val messageDao: MessageDao,
) : WhatsHelpRepo {

    override suspend fun saveHistory(whatsAppNumber: WhatsAppNumber, formattedNumber: String) {
        historyDao.delete(whatsAppNumber.code, whatsAppNumber.number)
        val history = History(
            whatsAppNumber = whatsAppNumber,
            formattedNumber = formattedNumber
        )
        historyDao.insert(history)
    }

    override fun getHistory(): Flow<List<History>> = historyDao.getAll()

    override suspend fun deleteHistory(history: History) = historyDao.delete(history)

    override suspend fun clearHistory() = historyDao.clear()

    override fun getMessages(): Flow<List<Message>> = messageDao.getAll()

    override suspend fun deleteMessage(message: Message) = messageDao.delete(message)

    override suspend fun deleteAllMessages() = messageDao.clear()

    override suspend fun addMessage(text: String) = messageDao.insert(Message(text = text))
}