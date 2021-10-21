package com.github.ktvipin27.whatshelp.data.model

/**
 * Created by Vipin KT on 21/10/21
 */
data class WhatsAppNumber(
    val code: Int?=null,
    val number: String,
) {
    val fullNumber: String
        get() = if (code != null) "+$code $number" else number
}
