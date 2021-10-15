package com.github.ktvipin27.whatshelp.di.module

import com.github.ktvipin27.whatshelp.data.repo.WhatsHelpRepo
import com.github.ktvipin27.whatshelp.data.repo.WhatsHelpRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by Vipin KT on 15/10/21
 */
@InstallIn(ViewModelComponent::class)
@Module
abstract class RepoModule {

    @Binds
    abstract fun provideWhatsHelpRepo(whatsHelpRepo: WhatsHelpRepoImpl): WhatsHelpRepo
}