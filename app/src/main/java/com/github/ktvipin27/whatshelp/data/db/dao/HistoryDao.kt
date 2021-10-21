package com.github.ktvipin27.whatshelp.data.db.dao

import androidx.room.*
import com.github.ktvipin27.whatshelp.data.db.entity.History
import com.github.ktvipin27.whatshelp.data.model.WhatsAppNumber

/**
 * Created by Vipin KT on 15/10/21
 */
@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY timeStamp DESC")
    suspend fun getAll(): List<History>

    @Insert
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)

    @Query("DELETE FROM history WHERE number=:number AND code=:code")
    suspend fun delete(code:Int?,number: String)

    @Query("DELETE FROM history")
    suspend fun clean()
}