package com.whatshelp.manager.app

/**
 * Created by Vipin KT on 02/11/21
 */
interface AppManager {
    fun isAppInstalled(packageName: String): Boolean
    fun shareText(text: String)
    fun openApplication(uriString: String, packageName: String? = null)
}