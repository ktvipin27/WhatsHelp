package com.whatshelp.manager.pref

/**
 * Created by Vipin KT on 04/11/21
 */
interface PreferencesManager {
    fun setTheme(theme: Int)
    fun getTheme(): Int
    fun getCopiedNumber(): Pair<String, Long>
    fun setCopiedNumber(pair: Pair<String, Long>)
}