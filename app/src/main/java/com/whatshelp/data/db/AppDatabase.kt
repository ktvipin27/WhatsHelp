package com.whatshelp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.whatshelp.data.db.dao.HistoryDao
import com.whatshelp.data.db.entity.History
import com.whatshelp.util.Constants.DB_NAME

/**
 * Created by Vipin KT on 15/10/21
 */
@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database.
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
        }
    }
}