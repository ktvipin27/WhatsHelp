package com.whatshelp.util

import android.content.Context
import android.content.pm.PackageManager
import android.provider.CallLog.Calls.*
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.work.Data
import com.google.android.gms.tasks.Task
import com.whatshelp.R
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

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

/**
 * Convenience method similar to [MediatorLiveData.addSource], except that it bulk adds sources
 * to this [MediatorLiveData] to listen to.
 */
fun <T> MediatorLiveData<T>.addSources(
    vararg sources: LiveData<out Any>,
    onChanged: Observer<Any>,
) {
    sources.forEach { source ->
        addSource(source, onChanged)
    }
}

fun <T> Data.Builder.putEnum(key: String, value: T) = apply { putString(key, value.toString()) }

inline fun <reified T : Enum<T>> Data.getEnum(key: String): T? {
    val enumValue = getString(key)
    return runCatching { enumValueOf<T>(enumValue!!) }.getOrNull()
}

suspend fun Task<Void>.complete(): Boolean =
    suspendCancellableCoroutine { continuation ->
        this.addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(true)
            } else {
                continuation.resume(false)
            }
        }
    }

/**
 * Binding adapter used for displaying call history log on screen.
 */
@BindingAdapter("imageType")
fun bindImageType(imgView: ImageView, imgType: Int?) {
    when (imgType) {
        OUTGOING_TYPE -> {
            imgView.setImageResource(R.drawable.ic_baseline_call_made_24)
        }
        in arrayOf(INCOMING_TYPE, REJECTED_TYPE) -> {
            imgView.setImageResource(R.drawable.ic_baseline_call_received_24)
        }
        MISSED_TYPE -> {
            imgView.setImageResource(R.drawable.ic_baseline_call_missed_24)
        }
    }
}

fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED