package com.github.ktvipin27.whatshelp.util

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Vipin KT on 16/10/21
 */
@AndroidEntryPoint
class WhatsTextInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = com.google.android.material.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyle) {

    private var onPasteListener: ((String) -> Unit)? = null

    @Inject lateinit var clipboardUtil: ClipboardUtil

    override fun onTextContextMenuItem(id: Int): Boolean {
        if (id == android.R.id.paste) {
            val text = clipboardUtil.getCopiedText()
            onPasteListener?.invoke(text)
            return true
        }
        return super.onTextContextMenuItem(id)
    }

    fun setOnPasteListener(listener: ((String) -> Unit)) {
        onPasteListener = listener
    }
}