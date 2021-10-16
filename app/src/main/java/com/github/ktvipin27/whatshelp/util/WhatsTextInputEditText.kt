package com.github.ktvipin27.whatshelp.util

import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

/**
 * Created by Vipin KT on 16/10/21
 */
class WhatsTextInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = com.google.android.material.R.attr.editTextStyle
) : TextInputEditText(context, attrs, defStyle) {

    private var onPasteListener: ((String) -> Unit)? = null

    private val clipboard: ClipboardManager? =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?


    override fun onTextContextMenuItem(id: Int): Boolean {
        if (id == android.R.id.paste) {
            var text = ""
            clipboard?.primaryClip?.getItemAt(0)?.text?.let {
                text = it.toString()
            }
            onPasteListener?.invoke(text)
            return true
        }
        return super.onTextContextMenuItem(id)
    }

    fun setOnPasteListener(listener: ((String) -> Unit)) {
        onPasteListener = listener
    }
}