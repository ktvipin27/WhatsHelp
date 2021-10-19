package com.github.ktvipin27.whatshelp.util

import android.content.ClipboardManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Vipin KT on 17/10/21
 */
@Singleton
class ClipboardUtil @Inject constructor(@ApplicationContext val context: Context) {

    private val clipboard: ClipboardManager? =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

    fun getCopiedText(): String {
        var text = ""
        clipboard?.primaryClip?.getItemAt(0)?.text?.let {
            text = it.toString()
        }
        return text
    }
}