package com.whatshelp.util

import java.text.SimpleDateFormat
import java.util.*

const val PATTERN_1 = "MMM dd hh:mm a"
const val PATTERN_2 = "MMM dd yyyy hh:mm a"
const val PATTERN_3 = "hh:mm a"

fun Long.getDateAgo(timeZoneId: String? = null): String {
    var time = this
    if (time < 1000000000000L) {
        time *= 1000
    }
    val smsTime = Calendar.getInstance()
    smsTime.timeInMillis = this
    val now = Calendar.getInstance()
    val pattern =
        if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) PATTERN_1 else PATTERN_2

    val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    val simpleTimeFormat = SimpleDateFormat(PATTERN_3, Locale.getDefault())
    if (timeZoneId != null)
        simpleDateFormat.timeZone = TimeZone.getTimeZone(timeZoneId)
    else
        simpleDateFormat.timeZone = TimeZone.getDefault()
    return when {
        now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) -> "Today ${
            simpleTimeFormat.format(Date(time))
        }"
        now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1 -> "Yesterday ${
            simpleTimeFormat.format(Date(time))
        }"
        else -> simpleDateFormat.format(Date(time))
    }
}