package com.github.ktvipin27.whatshelp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.BindingAdapter

/**
 * Created by Vipin KT on 16/10/21
 */
fun View.hideKeyboard() {
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

@BindingAdapter("visibility")
fun View.visible(isVisible:Boolean){
     this.visibility = if (isVisible) View.VISIBLE else View.GONE
}
