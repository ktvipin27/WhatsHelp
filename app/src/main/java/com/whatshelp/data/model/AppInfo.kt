package com.whatshelp.data.model

import com.whatshelp.BuildConfig

/**
 * Created by Vipin KT on 02/11/21
 */
data class AppInfo(
    val versionCode: Int = BuildConfig.VERSION_CODE,
    val versionName: String = BuildConfig.VERSION_NAME,
)
