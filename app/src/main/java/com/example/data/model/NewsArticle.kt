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
    val domain: String,
    val brandColor: Color,
    val logoText: String,
    val siteUrl: String,
    val description: String,
    val rssUrl: String,
    val faviconUrl: String = "https://www.google.com/s2/favicons?domain=$domain&sz=128"
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
            name = "Detikcom",
            shortName = "Detik",
            domain = "detik.com",
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
            domain = "kompas.com",
            brandColor = KompasColor,
            logoText = "K",
            siteUrl = "https://www.kompas.com",
            description = "Akurat, jernih, dan tepercaya untuk nusantara",
            rssUrl = "https://api-berita-indonesia.vercel.app/antara/terbaru/"
        ),
        NewsSource(
            id = "cnn",
            name = "CNN Indonesia",
            shortName = "CNN",
            domain = "cnnindonesia.com",
            brandColor = CnnColor,
            logoText = "CNN",
            siteUrl = "https://www.cnnindonesia.com",
            description = "Berita akurat dan mendalam dari dalam & luar negeri",
            rssUrl = "https://www.cnnindonesia.com/nasional/rss"
        ),
        NewsSource(
            id = "cnbc",
            name = "CNBC Indonesia",
            shortName = "CNBC",
            domain = "cnbcindonesia.com",
            brandColor = CnbcColor,
            logoText = "CNBC",
            siteUrl = "https://www.cnbcindonesia.com",
            description = "Navigasi bisnis, investasi, dan pasar modal",
            rssUrl = "https://www.cnbcindonesia.com/news/rss"
        ),
        NewsSource(
            id = "antara",
            name = "Antara News",
            shortName = "Antara",
            domain = "antaranews.com",
            brandColor = AntaraColor,
            logoText = "A",
            siteUrl = "https://www.antaranews.com",
            description = "Kantor Berita Nasional Indonesia resmi",
            rssUrl = "https://www.antaranews.com/rss/terkini.xml"
        ),
        NewsSource(
            id = "tempo",
            name = "Tempo.co",
            shortName = "Tempo",
            domain = "tempo.co",
            brandColor = TempoColor,
            logoText = "T",
            siteUrl = "https://www.tempo.co",
            description = "Jurnalisme independen dan investigatif",
            rssUrl = "https://rss.tempo.co/nasional"
        ),
        NewsSource(
            id = "republika",
            name = "Republika",
            shortName = "Republika",
            domain = "republika.co.id",
            brandColor = RepublikaColor,
            logoText = "R",
            siteUrl = "https://www.republika.co.id",
            description = "Berita nasional, khazanah Islam, dan ekonomi syariah",
            rssUrl = "https://republika.co.id/rss"
        ),
        NewsSource(
            id = "jawapos",
            name = "Jawa Pos",
            shortName = "JawaPos",
            domain = "jawapos.com",
            brandColor = JawaPosColor,
            logoText = "JP",
            siteUrl = "https://www.jawapos.com",
            description = "Selalu ada yang baru dari peristiwa terhangat",
            rssUrl = "https://api-berita-indonesia.vercel.app/antara/terbaru/"
        ),
        NewsSource(
            id = "suara",
            name = "Suara.com",
            shortName = "Suara",
            domain = "suara.com",
            brandColor = SuaraColor,
            logoText = "S",
            siteUrl = "https://www.suara.com",
            description = "Kabar peristiwa terkini, gaya hidup, dan viral",
            rssUrl = "https://www.suara.com/rss"
        ),
        NewsSource(
            id = "kumparan",
            name = "Kumparan",
            shortName = "Kumparan",
            domain = "kumparan.com",
            brandColor = KumparanColor,
            logoText = "k",
            siteUrl = "https://www.kumparan.com",
            description = "Platform media kolaboratif terdepan di Indonesia",
            rssUrl = "https://api-berita-indonesia.vercel.app/kumparan/terbaru/"
        ),
        NewsSource(
            id = "sindonews",
            name = "Sindonews",
            shortName = "Sindo",
            domain = "sindonews.com",
            brandColor = SindonewsColor,
            logoText = "SN",
            siteUrl = "https://www.sindonews.com",
            description = "Informasi tepercaya seputar daerah, nasional, dan global",
            rssUrl = "https://api-berita-indonesia.vercel.app/antara/terbaru/"
        ),
        NewsSource(
            id = "bisnis",
            name = "Bisnis.com",
            shortName = "Bisnis",
            domain = "bisnis.com",
            brandColor = BisnisColor,
            logoText = "B",
            siteUrl = "https://www.bisnis.com",
            description = "Rujukan utama berita ekonomi, perbankan, dan emiten",
            rssUrl = "https://api-berita-indonesia.vercel.app/antara/terbaru/"
        ),
        NewsSource(
            id = "liputan6",
            name = "Liputan6",
            shortName = "Liputan6",
            domain = "liputan6.com",
            brandColor = Liputan6Color,
            logoText = "6",
            siteUrl = "https://www.liputan6.com",
            description = "Aktual, tajam, dan tepercaya dari seluruh pelosok negeri",
            rssUrl = "https://api-berita-indonesia.vercel.app/antara/terbaru/"
        ),
        NewsSource(
            id = "merdeka",
            name = "Merdeka.com",
            shortName = "Merdeka",
            domain = "merdeka.com",
            brandColor = MerdekaColor,
            logoText = "M",
            siteUrl = "https://www.merdeka.com",
            description = "Berita terkini politik, peristiwa, dan inspirasi nusantara",
            rssUrl = "https://api-berita-indonesia.vercel.app/merdeka/terbaru/"
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

    fun getSourceFaviconUrl(sourceName: String): String? {
        return sources.firstOrNull { it.name.contains(sourceName, ignoreCase = true) || sourceName.contains(it.name, ignoreCase = true) }?.faviconUrl
    }
}
