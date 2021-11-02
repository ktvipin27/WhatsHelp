package com.whatshelp.di.module

import com.whatshelp.manager.rating.RatingsManager
import com.whatshelp.manager.rating.RatingsManagerImpl
import com.whatshelp.manager.share.ShareManager
import com.whatshelp.manager.share.ShareManagerImpl
import com.whatshelp.manager.task.TaskManager
import com.whatshelp.manager.task.TaskManagerImpl
import com.whatshelp.manager.whatsapp.WhatsAppManager
import com.whatshelp.manager.whatsapp.WhatsAppManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ManagerModule {

    @Binds
    fun bindTaskManager(taskManager: TaskManagerImpl): TaskManager

    @Binds
    fun bindRatingsManager(ratingsManager: RatingsManagerImpl): RatingsManager

    @Binds
    fun bindWhatsAppManager(whatsAppManager: WhatsAppManagerImpl): WhatsAppManager

    @Binds
    fun bindShareManager(shareManager: ShareManagerImpl): ShareManager
}
