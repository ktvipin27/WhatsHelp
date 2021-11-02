package com.whatshelp.manager.share

import android.content.Context
import android.content.Intent
import androidx.core.app.ShareCompat
import com.whatshelp.R
import com.whatshelp.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 02/11/21
 */
@Singleton
class ShareManagerImpl @Inject constructor(@ApplicationContext val context: Context) :
    ShareManager {

    override fun shareApp() {
        val shareIntent = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setChooserTitle(context.getString(R.string.message_share_app))
            .setSubject(context.getString(R.string.app_name))
            .setText(context.getString(R.string.message_share_app, Constants.PACKAGE_WHATSHELP))
            .createChooserIntent()
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

}