package com.whatshelp.manager.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ShareCompat
import com.whatshelp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 02/11/21
 */
@Singleton
class AppManagerImpl @Inject constructor(@ApplicationContext val context: Context) :
    AppManager {

    override fun isAppInstalled(packageName: String): Boolean = try {
        context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }

    override fun shareText(text: String) {
        val shareIntent = ShareCompat.IntentBuilder(context)
            .setType("text/plain")
            .setChooserTitle(context.getString(R.string.action_share))
            .setSubject(context.getString(R.string.app_name))
            .setText(text)
            .createChooserIntent()
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun openApplication(uriString: String, packageName: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        packageName?.let {
            intent.`package` = packageName
        }
        context.startActivity(intent)
    }

}