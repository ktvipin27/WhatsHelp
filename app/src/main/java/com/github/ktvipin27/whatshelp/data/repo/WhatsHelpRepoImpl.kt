package com.github.ktvipin27.whatshelp.data.repo

import com.github.ktvipin27.whatshelp.data.db.dao.HistoryDao
import com.github.ktvipin27.whatshelp.data.db.entity.History
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

/**
 * Created by Vipin KT on 15/10/21
 */
@ViewModelScoped
class WhatsHelpRepoImpl @Inject constructor(private val historyDao: HistoryDao) : WhatsHelpRepo {

    override suspend fun saveHistory(countryCode: String, number: String) {
        //remove "+ "  and code
        val numberWithoutCode = number.substring(countryCode.length + 2)
        val history = History(
            code = countryCode, number = numberWithoutCode,
            formattedFullNumber = number
        )
        historyDao.insert(history)
    }

    override suspend fun getHistory(): List<History> {
        return historyDao.getAll()
    }
}