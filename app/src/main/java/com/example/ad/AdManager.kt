package com.example.ad

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.secmtp.sdk.core.api.ATAdInfo
import com.secmtp.sdk.core.api.ATSDK
import com.secmtp.sdk.core.api.AdError
import com.secmtp.sdk.interstitial.api.ATInterstitial
import com.secmtp.sdk.interstitial.api.ATInterstitialListener
import com.secmtp.sdk.nativead.api.ATNative
import com.secmtp.sdk.nativead.api.ATNativeNetworkListener
import com.secmtp.sdk.nativead.api.NativeAd
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manajer Mediasi Iklan TopOn dengan Adapter Meta Audience Network (Facebook Ads).
 * Mengatur siklus hidup Interstitial Ad (15 klik berkelanjutan) dan Native Ad.
 */
object AdManager {

    private const val TAG = "AdManager"

    private var isInitialized = false
    private var adPreferences: AdPreferences? = null

    // Interstitial Ad
    private var interstitialAd: ATInterstitial? = null
    private var isInterstitialLoading = false
    private var pendingActionAfterInterstitial: (() -> Unit)? = null

    // Native Ad
    private var nativeAdLoader: ATNative? = null
    private val _currentNativeAd = MutableStateFlow<NativeAd?>(null)
    val currentNativeAd: StateFlow<NativeAd?> = _currentNativeAd.asStateFlow()

    private val mainHandler = Handler(Looper.getMainLooper())

    /**
     * Inisialisasi TopOn SDK dan Adapter Meta Audience Network.
     * Dipanggil satu kali saat aplikasi dimulai (di Application atau MainActivity).
     */
    fun init(context: Context) {
        if (isInitialized) return

        val appContext = context.applicationContext
        adPreferences = AdPreferences.getInstance(appContext)

        try {
            // Nonaktifkan log debug berlebih untuk mencegah peringatan diagnostik ADX internal TopOn
            ATSDK.setNetworkLogDebug(false)
            ATSDK.setPersonalizedAdStatus(ATSDK.PERSONALIZED)

            // Batasi jaringan mediasi hanya untuk Meta Audience Network (Facebook, Firm ID: 1)
            val nonFacebookFirmIds = listOf(
                "3", "4", "5", "6", "8", "9", "10", "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34",
                "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
                "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "66", "77", "88"
            )
            ATSDK.setForbidNetworkFirmIdList(nonFacebookFirmIds)
            ATSDK.setFilterNetworkFirmIdList(AdConfig.PLACEMENT_INTERSTITIAL, nonFacebookFirmIds)
            ATSDK.setFilterNetworkFirmIdList(AdConfig.PLACEMENT_NATIVE, nonFacebookFirmIds)
            ATSDK.setAllowedShowNetworkFirmIdList(AdConfig.PLACEMENT_INTERSTITIAL, listOf("1"))
            ATSDK.setAllowedShowNetworkFirmIdList(AdConfig.PLACEMENT_NATIVE, listOf("1"))

            // Inisialisasi TopOn SDK dengan App ID dan App Key
            ATSDK.init(appContext, AdConfig.TOPON_APP_ID, AdConfig.TOPON_APP_KEY)
            isInitialized = true
            Log.d(TAG, "TopOn SDK initialized successfully. Initial click count: ${adPreferences?.getClickCount()}")

            // Pra-muat (preload) iklan Interstitial
            initInterstitial(appContext)

            // Pra-muat (preload) iklan Native
            initNative(appContext)
        } catch (t: Throwable) {
            Log.e(TAG, "Failed to initialize TopOn SDK safely: ${t.message}", t)
        }
    }

    /**
     * Mengambil jumlah klik saat ini dari penyimpanan persisten.
     */
    fun getCurrentClickCount(): Int {
        return adPreferences?.getClickCount() ?: 0
    }

    /**
     * Menangani interaksi klik pengguna dengan aturan jeda 15 klik berkelanjutan.
     *
     * Setiap kali pengguna mengeklik item berita atau topik, hitungan klik bertambah
     * dan tersimpan secara persisten di disk.
     *
     * Jika aplikasi ditutup pada klik ke-7, saat dibuka kembali hitungan dimulai dari 8.
     * Tepat saat mencapai 15, iklan Interstitial akan ditampilkan (jika sudah siap),
     * lalu hitungan direset kembali ke 0.
     *
     * @param activity Activity untuk menampilkan iklan Interstitial
     * @param onAction Aksi lanjutan (misal: membuka artikel berita atau topik)
     */
    fun handleUserClick(activity: Activity, onAction: () -> Unit) {
        val prefs = adPreferences ?: AdPreferences.getInstance(activity)
        val newCount = prefs.incrementClickCount()
        Log.d(TAG, "User click registered: $newCount / ${AdConfig.INTERSTITIAL_CLICK_TARGET}")

        if (newCount >= AdConfig.INTERSTITIAL_CLICK_TARGET) {
            // Reset hitungan klik kembali ke 0
            prefs.resetClickCount()
            Log.d(TAG, "Target 15 clicks reached! Checking Interstitial readiness...")

            val ad = interstitialAd
            if (ad != null && ad.isAdReady) {
                Log.d(TAG, "Interstitial is ready, showing ad now.")
                pendingActionAfterInterstitial = onAction
                try {
                    ad.show(activity)
                } catch (t: Throwable) {
                    Log.e(TAG, "Error showing interstitial: ${t.message}", t)
                    // Jika terjadi error saat memanggil show, pastikan aksi pengguna tetap dieksekusi
                    executePendingAction()
                    loadInterstitial(activity)
                }
            } else {
                Log.d(TAG, "Interstitial not ready yet. Executing user action immediately and requesting load.")
                onAction()
                loadInterstitial(activity)
            }
        } else {
            // Belum mencapai 15 klik, langsung jalankan aksi pengguna
            onAction()
        }
    }

    private fun initInterstitial(context: Context) {
        if (interstitialAd == null) {
            interstitialAd = ATInterstitial(context, AdConfig.PLACEMENT_INTERSTITIAL).apply {
                setAdListener(object : ATInterstitialListener {
                    override fun onInterstitialAdLoaded() {
                        isInterstitialLoading = false
                        Log.d(TAG, "TopOn Interstitial ad loaded successfully.")
                    }

                    override fun onInterstitialAdLoadFail(adError: AdError?) {
                        isInterstitialLoading = false
                        Log.w(TAG, "TopOn Interstitial ad load failed: ${adError?.desc} (code: ${adError?.code})")
                    }

                    override fun onInterstitialAdClicked(adInfo: ATAdInfo?) {
                        Log.d(TAG, "TopOn Interstitial ad clicked: ${adInfo?.networkFirmId}")
                    }

                    override fun onInterstitialAdShow(adInfo: ATAdInfo?) {
                        Log.d(TAG, "TopOn Interstitial ad shown.")
                    }

                    override fun onInterstitialAdClose(adInfo: ATAdInfo?) {
                        Log.d(TAG, "TopOn Interstitial ad closed by user. Executing pending action and preloading next ad.")
                        executePendingAction()
                        // Pra-muat iklan berikutnya untuk putaran 15 klik selanjutnya
                        loadInterstitial(context)
                    }

                    override fun onInterstitialAdVideoStart(adInfo: ATAdInfo?) {}

                    override fun onInterstitialAdVideoEnd(adInfo: ATAdInfo?) {}

                    override fun onInterstitialAdVideoError(adError: AdError?) {
                        Log.w(TAG, "TopOn Interstitial video error: ${adError?.desc}")
                        executePendingAction()
                    }
                })
            }
        }
        loadInterstitial(context)
    }

    private fun loadInterstitial(context: Context) {
        val ad = interstitialAd ?: return
        if (!isInterstitialLoading && !ad.isAdReady) {
            isInterstitialLoading = true
            Log.d(TAG, "Requesting TopOn Interstitial ad...")
            try {
                ad.load(context)
            } catch (e: Exception) {
                isInterstitialLoading = false
                Log.e(TAG, "Exception during interstitial load: ${e.message}", e)
            }
        }
    }

    private fun executePendingAction() {
        mainHandler.post {
            pendingActionAfterInterstitial?.invoke()
            pendingActionAfterInterstitial = null
        }
    }

    // ==========================================
    // Native Ad Management
    // ==========================================

    private fun initNative(context: Context) {
        if (nativeAdLoader == null) {
            nativeAdLoader = ATNative(context, AdConfig.PLACEMENT_NATIVE, object : ATNativeNetworkListener {
                override fun onNativeAdLoaded() {
                    Log.d(TAG, "TopOn Native ad loaded successfully.")
                    val ad = nativeAdLoader?.nativeAd
                    _currentNativeAd.value = ad
                }

                override fun onNativeAdLoadFail(adError: AdError?) {
                    Log.w(TAG, "TopOn Native ad load failed: ${adError?.desc} (code: ${adError?.code})")
                }
            })
        }
        requestNativeAd()
    }

    fun requestNativeAd() {
        try {
            Log.d(TAG, "Requesting TopOn Native ad...")
            nativeAdLoader?.makeAdRequest()
        } catch (e: Exception) {
            Log.e(TAG, "Exception during native ad request: ${e.message}", e)
        }
    }
}
