package com.example.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent

import android.app.Activity
import android.content.ContextWrapper

fun Context.findActivity(): Activity? {
    var ctx: Context = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

object CustomTabsHelper {

    /**
     * Opens an article link using Chrome Custom Tabs with customized colors and animation,
     * falling back smoothly to standard external browser intent if needed.
     */
    fun openNewsArticle(
        context: Context,
        url: String,
        toolbarColor: Int? = null,
        secondaryToolbarColor: Int? = null
    ) {
        if (url.isBlank()) {
            Toast.makeText(context, "Tautan artikel tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val uri = Uri.parse(url)
            val builder = CustomTabsIntent.Builder()

            // Enable article title display in browser header
            builder.setShowTitle(true)

            // Enable native share action inside Custom Tab menu
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)

            // Customize colors if provided
            if (toolbarColor != null) {
                val colorParams = CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(toolbarColor)
                    .apply {
                        if (secondaryToolbarColor != null) {
                            setSecondaryToolbarColor(secondaryToolbarColor)
                        }
                    }
                    .build()
                builder.setDefaultColorSchemeParams(colorParams)
            }

            // Animate transition (slide in from right, slide out to right)
            builder.setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            builder.setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)

            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, uri)
        } catch (e: ActivityNotFoundException) {
            // Fallback to standard ACTION_VIEW
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (fallbackError: Exception) {
                Toast.makeText(context, "Tidak dapat membuka tautan: ${fallbackError.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // General exception safeguard
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (_: Exception) {
                Toast.makeText(context, "Gagal membuka artikel", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Share article via Android system share sheet
     */
    fun shareArticle(context: Context, title: String, url: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, "$title\n\nBaca selengkapnya di: $url\n\n(Ditemukan via Berita Indonesia)")
        }
        val chooser = Intent.createChooser(shareIntent, "Bagikan Artikel Berita")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    /**
     * Share the Berita Indonesia application with others
     */
    fun shareApp(context: Context) {
        val appId = "com.beritaindonesia.gecckocreator"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Aplikasi Berita Indonesia")
            putExtra(
                Intent.EXTRA_TEXT,
                "Dapatkan berita Indonesia terkini, terpercaya, dan tercepat (Detik, Kompas, CNN, Antara, CNBC, dan lainnya) dalam satu aplikasi Berita Indonesia!\n\nUnduh gratis di Google Play Store:\nhttps://play.google.com/store/apps/details?id=$appId"
            )
        }
        val chooser = Intent.createChooser(shareIntent, "Bagikan Aplikasi Berita Indonesia")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    /**
     * Open Google Play Store listing to rate the application
     */
    fun rateApp(context: Context) {
        val appId = "com.beritaindonesia.gecckocreator"
        try {
            val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            }
            context.startActivity(marketIntent)
        } catch (e: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appId")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        }
    }
}
