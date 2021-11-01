package com.whatshelp.data.model

/**
 * Created by Vipin KT on 02/11/21
 */
data class Feedback(
    val type: FeedbackType,
    val feedback: String,
    val timestamp: Long,
    val time: String,
    val appInfo: AppInfo,
    val deviceInfo: DeviceInfo,
)
