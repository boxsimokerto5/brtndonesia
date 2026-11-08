package com.secmtp.sdk.mediation.adapters.pubnative

import android.app.Activity
import android.content.Context
import com.secmtp.sdk.interstitial.unitgroup.api.CustomInterstitialAdapter

class PubNativeInterstitialAdapter : CustomInterstitialAdapter() {
    override fun destory() {}
    override fun getNetworkName(): String = "PubNative"
    override fun getNetworkPlacementId(): String = ""
    override fun getNetworkSDKVersion(): String = "1.0.0"
    override fun isAdReady(): Boolean = false
    override fun show(activity: Activity?) {}
    override fun loadCustomNetworkAd(
        context: Context?,
        serverExtra: MutableMap<String, Any>?,
        localExtra: MutableMap<String, Any>?
    ) {
        notifyATLoadFail("10001", "PubNative adapter disabled in favor of Meta Audience Network")
    }
}
