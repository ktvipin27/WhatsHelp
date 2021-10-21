package com.github.ktvipin27.whatshelp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment

/**
 * Created by Vipin KT on 16/10/21
 */
fun View.hideKeyboard() {
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

@BindingAdapter("visibility")
fun View.visible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun Fragment.toast(messageResId: Int) = requireContext().toast(messageResId)

fun Fragment.toast(message: String) = requireContext().toast(message)

fun Context.toast(messageResId: Int) =
    Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show()

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()