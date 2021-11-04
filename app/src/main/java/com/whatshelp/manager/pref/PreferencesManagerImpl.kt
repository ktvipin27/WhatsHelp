package com.whatshelp.manager.pref

import android.content.SharedPreferences
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

    override fun setTheme(theme: Int) {
        preferences.edit()
            .putInt(PREF_THEME, theme)
            .apply()
    }

    override fun getTheme(): Int = preferences.getInt(PREF_THEME, 1)
}