package com.whatshelp.manager.share

import android.content.Context
import com.whatshelp.BuildConfig
import com.whatshelp.R
import com.whatshelp.manager.app.AppManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 02/11/21
 */
@Singleton
class ShareManagerImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val appManager: AppManager,
) : ShareManager {

    override fun shareApp() {
        val text = context.getString(R.string.message_share_app, BuildConfig.APPLICATION_ID)
        appManager.shareText(text)
    }

}