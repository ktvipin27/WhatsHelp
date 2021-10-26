package com.github.ktvipin27.whatshelp.data.db.dao

import androidx.room.*
import com.github.ktvipin27.whatshelp.data.db.entity.Message
import kotlinx.coroutines.flow.Flow

/**
 * Created by Vipin KT on 15/10/21
 */
@Dao
interface MessageDao {

    @Query("SELECT * FROM messages ORDER BY timeStamp DESC")
    fun getAll(): Flow<List<Message>>

    @Insert
    suspend fun insert(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(messages: List<Message>)

    @Delete
    suspend fun delete(message: Message)

    @Query("DELETE FROM messages")
    suspend fun clean()
}