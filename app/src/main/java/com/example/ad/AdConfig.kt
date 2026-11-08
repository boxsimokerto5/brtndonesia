package com.example.ad

/**
 * Konfigurasi Iklan TopOn Mediation dan Adapter Meta Audience Network (Facebook Ads).
 *
 * Pengembang dapat mengganti APP_ID, APP_KEY, dan PLACEMENT_ID di bawah ini
 * dengan ID akun TopOn dan Meta Audience Network milik Anda.
 */
object AdConfig {
    // TopOn App ID & App Key resmi dari TopOn (aktif dan terdaftar di server mediasi)
    const val TOPON_APP_ID = "a5aa1f9deda26d"
    const val TOPON_APP_KEY = "4f7b9ac17decb9babec83aac078742c7"

    // TopOn Placement IDs resmi (Hanya Interstitial dan Native Ad sesuai permintaan pengguna)
    const val PLACEMENT_INTERSTITIAL = "b5baca53984692"
    const val PLACEMENT_NATIVE = "b5aa1fa2cae775"

    // Target jeda klik berkelanjutan untuk iklan Interstitial (Tepat 15 klik)
    const val INTERSTITIAL_CLICK_TARGET = 15
}
