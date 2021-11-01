package com.whatshelp.di.module

import com.whatshelp.task.WHTaskManager
import com.whatshelp.task.WHTaskManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface TaskManagerModule {

    @Binds
    fun bindWHTaskManager(whTaskManager: WHTaskManagerImpl): WHTaskManager
}
