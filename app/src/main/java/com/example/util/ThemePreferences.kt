package com.example.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Penyimpanan preferensi tema aplikasi (Mode Terang / Mode Gelap).
 * Standar awal saat membuka aplikasi pertama kali adalah Mode Terang (Light Mode).
 */
class ThemePreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "app_theme_preferences"
        private const val KEY_IS_DARK_MODE = "key_is_dark_mode"

        @Volatile
        private var INSTANCE: ThemePreferences? = null

        fun getInstance(context: Context): ThemePreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ThemePreferences(context).also { INSTANCE = it }
            }
        }
    }

    /**
     * Memeriksa apakah mode gelap aktif.
     * Default adalah FALSE (Mode Terang) saat pertama kali diinstal/dibuka.
     */
    fun isDarkMode(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_DARK_MODE, false)
    }

    /**
     * Menyimpan pilihan preferensi tema pengguna.
     */
    fun setDarkMode(isDark: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_DARK_MODE, isDark).apply()
    }
}
