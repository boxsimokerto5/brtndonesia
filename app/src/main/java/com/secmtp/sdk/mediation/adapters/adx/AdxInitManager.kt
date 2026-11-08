package com.secmtp.sdk.mediation.adapters.adx

import android.content.Context
import com.secmtp.sdk.core.api.ATInitMediation
import com.secmtp.sdk.core.api.MediationInitCallback

/**
 * Adapter stub untuk TopOn ADX untuk memenuhi pengecekan internal TopOn SDK
 * saat hanya menggunakan mediasi Meta Audience Network.
 */
class AdxInitManager private constructor() : ATInitMediation() {

    override fun initSDK(
        context: Context?,
        serviceExtra: MutableMap<String, Any>?,
        callback: MediationInitCallback?
    ) {
        callback?.onSuccess()
    }

    override fun getNetworkName(): String = "ADX"

    override fun getNetworkVersion(): String = "1.0.0"

    override fun getNetworkSDKClass(): String = AdxInitManager::class.java.name

    companion object {
        @Volatile
        private var instance: AdxInitManager? = null

        @JvmStatic
        fun getInstance(): AdxInitManager {
            return instance ?: synchronized(this) {
                instance ?: AdxInitManager().also { instance = it }
            }
        }
    }
}
