package com.example.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimeUtils {

    private val rfc822Format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }

    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val simpleIsoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)

    private val idDateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))

    fun formatRelativeTime(dateString: String): String {
        if (dateString.isBlank()) return "Baru saja"

        val parsedDate = parseDate(dateString) ?: return dateString

        val now = System.currentTimeMillis()
        val diff = now - parsedDate.time

        if (diff < 0) return "Baru saja"

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            minutes < 2 -> "Baru saja"
            minutes < 60 -> "$minutes mnt lalu"
            hours < 24 -> "$hours jam lalu"
            days == 1L -> "Kemarin"
            days < 7 -> "$days hari lalu"
            else -> idDateFormat.format(parsedDate)
        }
    }

    private fun parseDate(dateString: String): Date? {
        val trimmed = dateString.trim()
        val formats = listOf(
            rfc822Format,
            SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH),
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH),
            isoFormat,
            simpleIsoFormat
        )

        for (format in formats) {
            try {
                return format.parse(trimmed)
            } catch (_: Exception) {
                // Try next pattern
            }
        }
        return null
    }
}
