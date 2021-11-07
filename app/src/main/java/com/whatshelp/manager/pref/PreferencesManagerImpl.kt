package com.whatshelp.manager.pref

import android.content.SharedPreferences
import com.whatshelp.util.Constants.Preferences.PREF_COPIED_NUMBER
import com.whatshelp.util.Constants.Preferences.PREF_COPIED_TIME
import com.whatshelp.util.Constants.Preferences.PREF_THEME
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 04/11/21
 */
@Singleton
class PreferencesManagerImpl @Inject constructor(
    private val preferences: SharedPreferences,
) : PreferencesManager {

    override fun setTheme(theme: Int) = preferences.edit()
        .putInt(PREF_THEME, theme)
        .apply()

    override fun getTheme(): Int = preferences.getInt(PREF_THEME, 1)

    override fun getCopiedNumber(): Pair<String, Long> =
        Pair(preferences.getString(PREF_COPIED_NUMBER, "") ?: "",
            preferences.getLong(PREF_COPIED_TIME, 0))

    override fun setCopiedNumber(pair: Pair<String, Long>) = preferences.edit()
        .putString(PREF_COPIED_NUMBER, pair.first)
        .putLong(PREF_COPIED_TIME, pair.second)
        .apply()
}