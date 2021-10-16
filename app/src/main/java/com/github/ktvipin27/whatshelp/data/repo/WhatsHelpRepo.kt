package com.github.ktvipin27.whatshelp.data.repo

import com.github.ktvipin27.whatshelp.data.db.entity.History

/**
 * Created by Vipin KT on 15/10/21
 */
interface WhatsHelpRepo {

    suspend fun saveHistory(countryCode:String,number: String)

    suspend fun getHistory(): List<History>
}