package com.whatshelp.util

import android.text.style.URLSpan
import android.view.View

/**
 * Created by Vipin KT on 02/11/21
 */
class URLSpanWithListener(url: String, private val listener: () -> Unit) : URLSpan(url) {
    override fun onClick(widget: View) {
        super.onClick(widget)
        listener()
    }
}