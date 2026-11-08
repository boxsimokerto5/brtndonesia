package com.secmtp.sdk.mediation.adapters.pubnative

import android.content.Context
import com.secmtp.sdk.nativead.unitgroup.api.CustomNativeAdapter

class PubNativeAdapter : CustomNativeAdapter() {
    override fun destory() {}
    override fun getNetworkName(): String = "PubNative"
    override fun getNetworkPlacementId(): String = ""
    override fun getNetworkSDKVersion(): String = "1.0.0"
    override fun loadCustomNetworkAd(
        context: Context?,
        serverExtra: MutableMap<String, Any>?,
        localExtra: MutableMap<String, Any>?
    ) {
        notifyATLoadFail("10001", "PubNative adapter disabled in favor of Meta Audience Network")
    }
}
