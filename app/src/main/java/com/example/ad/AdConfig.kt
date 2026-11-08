package com.example.ad

/**
 * Konfigurasi Iklan TopOn Mediation dan Adapter Meta Audience Network (Facebook Ads).
 *
 * Pengembang dapat mengganti APP_ID, APP_KEY, dan PLACEMENT_ID di bawah ini
 * dengan ID akun TopOn dan Meta Audience Network milik Anda.
 */
object AdConfig {
    // TopOn App ID & App Key resmi dari TopOn (aktif dan terdaftar di server mediasi)
    const val TOPON_APP_ID = "h6aedf2fbc940c"
    const val TOPON_APP_KEY = "a7fdeff07720eef7c7473235707ddf3d0"

    // TopOn Placement IDs resmi (Hanya Interstitial dan Native Ad sesuai permintaan pengguna)
    const val PLACEMENT_INTERSTITIAL = "n6aef3a1524ca3"
    const val PLACEMENT_NATIVE = "n6aef398145b52"

    // Target jeda klik berkelanjutan untuk iklan Interstitial (Tepat 15 klik)
    const val INTERSTITIAL_CLICK_TARGET = 15
}
