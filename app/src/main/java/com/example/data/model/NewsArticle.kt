package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

enum class NewsCategory(val displayName: String) {
    BERITA_UTAMA("BERITA UTAMA"),
    TEKNOLOGI("TEKNOLOGI"),
    OLAHRAGA("OLAHRAGA"),
    EKONOMI("EKONOMI"),
    NASIONAL("NASIONAL"),
    HIBURAN("HIBURAN")
}

data class NewsSource(
    val id: String,
    val name: String,
    val shortName: String,
    val brandColor: Color,
    val logoText: String,
    val siteUrl: String,
    val description: String,
    val rssUrl: String
)

data class NewsArticle(
    val id: String,
    val title: String,
    val link: String,
    val description: String,
    val pubDate: String,
    val imageUrl: String? = null,
    val sourceName: String,
    val category: String = "BERITA UTAMA",
    val author: String? = null,
    val isBookmarked: Boolean = false
)

object SourceCatalog {
    val sources = listOf(
        NewsSource(
            id = "detik",
            name = "Detik.com",
            shortName = "Detik",
            brandColor = DetikColor,
            logoText = "d",
            siteUrl = "https://www.detik.com",
            description = "Portal berita terdepan & terpopuler di Indonesia",
            rssUrl = "https://api-berita-indonesia.vercel.app/detik/terbaru/"
        ),
        NewsSource(
            id = "kompas",
            name = "Kompas.com",
            shortName = "Kompas",
            brandColor = KompasColor,
            logoText = "K",
            siteUrl = "https://www.kompas.com",
            description = "Akurat, jernih, dan tepercaya untuk nusantara",
            rssUrl = "https://api-berita-indonesia.vercel.app/antara/terbaru/"
        ),
        NewsSource(
            id = "jawapos",
            name = "Jawa Pos",
            shortName = "JawaPos",
            brandColor = JawaPosColor,
            logoText = "JP",
            siteUrl = "https://www.jawapos.com",
            description = "Selalu ada yang baru dari peristiwa terhangat",
            rssUrl = "https://api-berita-indonesia.vercel.app/antara/terbaru/"
        ),
        NewsSource(
            id = "cnn",
            name = "CNN Indonesia",
            shortName = "CNN",
            brandColor = CnnColor,
            logoText = "CNN",
            siteUrl = "https://www.cnnindonesia.com",
            description = "Berita akurat dan mendalam dari dalam & luar negeri",
            rssUrl = "https://www.cnnindonesia.com/nasional/rss"
        ),
        NewsSource(
            id = "antara",
            name = "Antara News",
            shortName = "Antara",
            brandColor = AntaraColor,
            logoText = "A",
            siteUrl = "https://www.antaranews.com",
            description = "Kantor Berita Nasional Indonesia resmi",
            rssUrl = "https://www.antaranews.com/rss/terkini.xml"
        ),
        NewsSource(
            id = "cnbc",
            name = "CNBC Indonesia",
            shortName = "CNBC",
            brandColor = CnbcColor,
            logoText = "CNBC",
            siteUrl = "https://www.cnbcindonesia.com",
            description = "Navigasi bisnis, investasi, dan teknologi finansial",
            rssUrl = "https://www.cnbcindonesia.com/news/rss"
        ),
        NewsSource(
            id = "tempo",
            name = "Tempo.co",
            shortName = "Tempo",
            brandColor = TempoColor,
            logoText = "T",
            siteUrl = "https://www.tempo.co",
            description = "Jurnalisme independen dan investigatif",
            rssUrl = "https://rss.tempo.co/nasional"
        )
    )

    fun getSourceColor(sourceName: String): Color {
        return sources.firstOrNull { it.name.contains(sourceName, ignoreCase = true) || sourceName.contains(it.name, ignoreCase = true) }?.brandColor
            ?: NusantaraBlueActive
    }

    fun getSourceLogoText(sourceName: String): String {
        return sources.firstOrNull { it.name.contains(sourceName, ignoreCase = true) || sourceName.contains(it.name, ignoreCase = true) }?.logoText
            ?: sourceName.take(1).uppercase()
    }
}
