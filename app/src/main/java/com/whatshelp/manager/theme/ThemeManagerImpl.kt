package com.whatshelp.manager.theme

import androidx.appcompat.app.AppCompatDelegate
import com.whatshelp.manager.pref.PreferencesManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 04/11/21
 */
@Singleton
class ThemeManagerImpl @Inject constructor(
    private val preferences: PreferencesManager,
) : ThemeManager {

    private var pTheme: Int
        get() = preferences.getTheme()
        set(value) {
            preferences.setTheme(value)
        }

    override fun setTheme() {
        AppCompatDelegate.setDefaultNightMode(pTheme)
    }

    override fun setDefaultTheme() = setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    override fun setDarkTheme() = setTheme(AppCompatDelegate.MODE_NIGHT_YES)

    override fun setLightTheme() = setTheme(AppCompatDelegate.MODE_NIGHT_NO)

    private fun setTheme(theme: Int) {
        AppCompatDelegate.setDefaultNightMode(theme)
        pTheme = theme
    }

}