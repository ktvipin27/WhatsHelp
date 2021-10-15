package com.github.ktvipin27.whatshelp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.github.ktvipin27.whatshelp.data.db.entity.History

/**
 * Created by Vipin KT on 15/10/21
 */
@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    suspend fun getAll(): List<History>

    @Insert
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)

    @Query("DELETE FROM history")
    suspend fun clean()
}