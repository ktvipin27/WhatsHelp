package com.whatshelp.di.module

import android.content.Context
import com.whatshelp.data.db.AppDatabase
import com.whatshelp.data.db.dao.HistoryDao
import com.whatshelp.data.db.dao.MessageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Vipin KT on 15/10/21
 */
@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideHistoryDao(appDatabase: AppDatabase): HistoryDao = appDatabase.historyDao()

    @Provides
    fun provideMessageDao(appDatabase: AppDatabase): MessageDao = appDatabase.messageDao()
}