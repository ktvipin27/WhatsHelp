package com.github.ktvipin27.whatshelp.data.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.github.ktvipin27.whatshelp.data.db.Migrations.MIGRATION_1_2
import com.github.ktvipin27.whatshelp.data.db.dao.HistoryDao
import com.github.ktvipin27.whatshelp.data.db.dao.MessageDao
import com.github.ktvipin27.whatshelp.data.db.entity.History
import com.github.ktvipin27.whatshelp.data.db.entity.Message
import com.github.ktvipin27.whatshelp.util.Constants.DB_NAME
import com.github.ktvipin27.whatshelp.worker.DatabasePopulatingWorker

/**
 * Created by Vipin KT on 15/10/21
 */
@Database(entities = [History::class, Message::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    abstract fun messageDao(): MessageDao

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
            val callback = object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val workRequest: WorkRequest =
                        OneTimeWorkRequestBuilder<DatabasePopulatingWorker>()
                            .build()
                    WorkManager
                        .getInstance(context)
                        .enqueue(workRequest)
                }
            }
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .addCallback(callback)
                .build()
        }
    }
}