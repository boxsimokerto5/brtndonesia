package com.example.ui.components

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.load
import com.example.ad.AdManager
import com.example.ui.theme.DarkNusantaraSurface
import com.example.ui.theme.NusantaraBlueActive
import com.secmtp.sdk.nativead.api.ATNativeAdView
import com.secmtp.sdk.nativead.api.ATNativePrepareInfo
import com.secmtp.sdk.nativead.api.NativeAd

/**
 * Komponen Native Ad untuk disematkan di antara daftar feed berita.
 * Mendukung rendering native ad SDK TopOn / Meta Audience Network serta
 * fallback UI yang elegan jika iklan sedang dimuat.
 */
@Composable
fun NativeAdCard(
    modifier: Modifier = Modifier
) {
    val nativeAd by AdManager.currentNativeAd.collectAsState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("native_ad_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (nativeAd != null) {
            // Render via TopOn ATNativeAdView
            TopOnNativeAdViewContainer(nativeAd = nativeAd!!)
        } else {
            // Placeholder / loading state yang rapi dan elegan
            FallbackNativeAdCard()
        }
    }
}

@Composable
private fun TopOnNativeAdViewContainer(
    nativeAd: NativeAd,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            val atNativeAdView = ATNativeAdView(ctx)
            val adLayout = buildNativeAdLayout(ctx, nativeAd, atNativeAdView)
            atNativeAdView.addView(adLayout)
            atNativeAdView
        },
        update = { atNativeAdView ->
            atNativeAdView.removeAllViews()
            val adLayout = buildNativeAdLayout(context, nativeAd, atNativeAdView)
            atNativeAdView.addView(adLayout)
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}

/**
 * Menyusun tata letak Native Ad Android View dan mendaftarkan views ke ATNativePrepareInfo
 * agar impression & click tracking Meta Audience Network / TopOn berfungsi akurat.
 */
private fun buildNativeAdLayout(
    context: Context,
    nativeAd: NativeAd,
    containerView: ATNativeAdView
): View {
    val material = nativeAd.adMaterial
    val layout = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dpToPx(context, 14), dpToPx(context, 14), dpToPx(context, 14), dpToPx(context, 14))
        layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
    }

    // Top Header: Badge "IKLAN / SPONSORED" & Sumber Iklan
    val headerLayout = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dpToPx(context, 8)
        }
    }

    val adBadge = TextView(context).apply {
        text = "IKLAN SPONSOR"
        textSize = 10f
        setTypeface(null, Typeface.BOLD)
        setTextColor(android.graphics.Color.WHITE)
        setBackgroundColor(android.graphics.Color.parseColor("#0D47A1"))
        setPadding(dpToPx(context, 6), dpToPx(context, 2), dpToPx(context, 6), dpToPx(context, 2))
    }
    headerLayout.addView(adBadge)

    val adFromText = material?.adFrom ?: "TopOn & Meta Audience Network"
    val adFromView = TextView(context).apply {
        text = "  •  $adFromText"
        textSize = 11f
        setTextColor(android.graphics.Color.GRAY)
    }
    headerLayout.addView(adFromView)
    layout.addView(headerLayout)

    // Media / Image View
    val mediaView = material?.getAdMediaView()
    val mainImageView = ImageView(context)
    if (mediaView != null) {
        val mediaParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 160)
        ).apply {
            bottomMargin = dpToPx(context, 10)
        }
        layout.addView(mediaView, mediaParams)
    } else if (!material?.mainImageUrl.isNullOrBlank()) {
        mainImageView.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(context, 160)
            ).apply {
                bottomMargin = dpToPx(context, 10)
            }
            load(material?.mainImageUrl)
        }
        layout.addView(mainImageView)
    }

    // Title View
    val titleView = TextView(context).apply {
        text = material?.title ?: "Informasi Sponsor Terpilih"
        textSize = 15f
        setTypeface(null, Typeface.BOLD)
        maxLines = 2
        ellipsize = TextUtils.TruncateAt.END
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dpToPx(context, 4)
        }
    }
    layout.addView(titleView)

    // Description View
    val descView = TextView(context).apply {
        text = material?.descriptionText ?: "Dapatkan update promosi dan penawaran terbaik khusus pengguna Berita Indonesia."
        textSize = 13f
        maxLines = 2
        ellipsize = TextUtils.TruncateAt.END
        setTextColor(android.graphics.Color.GRAY)
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dpToPx(context, 10)
        }
    }
    layout.addView(descView)

    // CTA Button
    val ctaButton = Button(context).apply {
        text = material?.callToActionText ?: "Pelajari Lebih Lanjut"
        textSize = 13f
        setTypeface(null, Typeface.BOLD)
        setTextColor(android.graphics.Color.WHITE)
        setBackgroundColor(android.graphics.Color.parseColor("#0D47A1"))
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(context, 44)
        )
    }
    layout.addView(ctaButton)

    // Bind Views via ATNativePrepareInfo
    val prepareInfo = ATNativePrepareInfo().apply {
        parentView = layout
        setTitleView(titleView)
        setDescView(descView)
        setCtaView(ctaButton)
        if (mediaView == null && !material?.mainImageUrl.isNullOrBlank()) {
            setMainImageView(mainImageView)
        }
        setClickViewList(listOf(ctaButton, titleView))
    }

    try {
        nativeAd.prepare(containerView, prepareInfo)
    } catch (e: Exception) {
        android.util.Log.e("NativeAdCard", "Failed to prepare native ad view: ${e.message}")
    }

    return layout
}

private fun dpToPx(context: Context, dp: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

/**
 * Fallback UI ketika Native Ad sedang dalam proses pemuatan jaringan
 */
@Composable
private fun FallbackNativeAdCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = NusantaraBlueActive
            ) {
                Text(
                    text = "IKLAN SPONSOR",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Text(
                text = "TopOn & Meta Network",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = NusantaraBlueActive.copy(alpha = 0.12f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Iklan",
                        tint = NusantaraBlueActive,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Temukan Penawaran & Berita Pilihan",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Sponsor terverifikasi dari jaringan mitra resmi.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        FilledTonalButton(
            onClick = {
                // Minta reload iklan native baru dari TopOn
                AdManager.requestNativeAd()
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = NusantaraBlueActive.copy(alpha = 0.15f),
                contentColor = NusantaraBlueActive
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pelajari Selengkapnya", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}
