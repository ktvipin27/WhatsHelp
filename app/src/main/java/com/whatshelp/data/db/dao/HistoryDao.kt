package com.whatshelp.data.db.dao

import androidx.room.*
import com.whatshelp.data.db.entity.History
import kotlinx.coroutines.flow.Flow

/**
 * Created by Vipin KT on 15/10/21
 */
@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY timeStamp DESC")
    fun getAll(): Flow<List<History>>

    @Insert
    suspend fun insert(history: History)

    @Delete
    suspend fun delete(history: History)

    @Query("DELETE FROM history WHERE number=:number AND code=:code")
    suspend fun delete(code:Int?,number: String)

    @Query("DELETE FROM history")
    suspend fun clear()
}