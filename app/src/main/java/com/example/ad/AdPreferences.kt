package com.example.ad

import android.content.Context
import android.content.SharedPreferences

/**
 * Pengelola penyimpanan SharedPreferences untuk penghitung klik iklan interstitial.
 * Memastikan hitungan klik tetap tersimpan dan berkelanjutan meskipun aplikasi
 * ditutup, dimatikan (kill process), atau dibuka kembali.
 */
class AdPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "ad_counter_preferences"
        private const val KEY_PERSISTENT_CLICK_COUNT = "key_persistent_click_count"

        @Volatile
        private var INSTANCE: AdPreferences? = null

        fun getInstance(context: Context): AdPreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AdPreferences(context).also { INSTANCE = it }
            }
        }
    }

    /**
     * Mengambil jumlah klik saat ini yang tersimpan di disk.
     */
    fun getClickCount(): Int {
        return sharedPreferences.getInt(KEY_PERSISTENT_CLICK_COUNT, 0)
    }

    /**
     * Menambahkan 1 ke hitungan klik yang sedang berjalan dan menyimpannya secara persisten.
     * Mengembalikan nilai hitungan yang baru.
     */
    fun incrementClickCount(): Int {
        val current = getClickCount()
        val next = current + 1
        sharedPreferences.edit().putInt(KEY_PERSISTENT_CLICK_COUNT, next).apply()
        return next
    }

    /**
     * Mengatur ulang hitungan klik kembali ke 0 setelah iklan Interstitial ditampilkan.
     */
    fun resetClickCount() {
        sharedPreferences.edit().putInt(KEY_PERSISTENT_CLICK_COUNT, 0).apply()
    }
}
