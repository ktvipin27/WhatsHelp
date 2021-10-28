package com.whatshelp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Vipin KT on 21/10/21
 */
@Parcelize
data class WhatsAppNumber(
    val code: Int? = null,
    val number: String,
) : Parcelable {
    val fullNumber: String
        get() = if (code != null) "+$code $number" else number
}
