package com.example.data.repository

import com.example.data.local.BookmarkDao
import com.example.data.local.BookmarkEntity
import com.example.data.model.NewsArticle
import com.example.data.model.NewsCategory
import com.example.data.remote.NewsDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val bookmarkDao: BookmarkDao,
    private val dataSource: NewsDataSource = NewsDataSource()
) {

    val bookmarkedArticles: Flow<List<NewsArticle>> = bookmarkDao.getAllBookmarks().map { entities ->
        entities.map { it.toNewsArticle() }
    }

    val bookmarkedUrls: Flow<Set<String>> = bookmarkDao.getAllBookmarkedUrls().map { urls ->
        urls.toSet()
    }

    suspend fun fetchAllLatestNews(): List<NewsArticle> = coroutineScope {
        // Concurrently fetch across premier Indonesian news providers via official direct RSS feeds
        val feedDeferreds = listOf(
            // Detikcom
            async { dataSource.fetchFromRssUrl("https://news.detik.com/rss", "Detikcom", "BERITA UTAMA") },
            async { dataSource.fetchFromRssUrl("https://finance.detik.com/rss", "Detikcom", "EKONOMI") },
            async { dataSource.fetchFromRssUrl("https://inet.detik.com/rss", "Detikcom", "TEKNOLOGI") },
            async { dataSource.fetchFromRssUrl("https://sport.detik.com/rss", "Detikcom", "OLAHRAGA") },

            // CNN Indonesia
            async { dataSource.fetchFromRssUrl("https://www.cnnindonesia.com/nasional/rss", "CNN Indonesia", "NASIONAL") },
            async { dataSource.fetchFromRssUrl("https://www.cnnindonesia.com/ekonomi/rss", "CNN Indonesia", "EKONOMI") },
            async { dataSource.fetchFromRssUrl("https://www.cnnindonesia.com/olahraga/rss", "CNN Indonesia", "OLAHRAGA") },
            async { dataSource.fetchFromRssUrl("https://www.cnnindonesia.com/teknologi/rss", "CNN Indonesia", "TEKNOLOGI") },
            async { dataSource.fetchFromRssUrl("https://www.cnnindonesia.com/hiburan/rss", "CNN Indonesia", "HIBURAN") },

            // CNBC Indonesia
            async { dataSource.fetchFromRssUrl("https://www.cnbcindonesia.com/news/rss", "CNBC Indonesia", "BERITA UTAMA") },
            async { dataSource.fetchFromRssUrl("https://www.cnbcindonesia.com/market/rss", "CNBC Indonesia", "EKONOMI") },
            async { dataSource.fetchFromRssUrl("https://www.cnbcindonesia.com/tech/rss", "CNBC Indonesia", "TEKNOLOGI") },

            // Antara News
            async { dataSource.fetchFromRssUrl("https://www.antaranews.com/rss/terkini.xml", "Antara News", "BERITA UTAMA") },
            async { dataSource.fetchFromRssUrl("https://www.antaranews.com/rss/top-news.xml", "Antara News", "BERITA UTAMA") },
            async { dataSource.fetchFromRssUrl("https://www.antaranews.com/rss/politik.xml", "Antara News", "NASIONAL") },
            async { dataSource.fetchFromRssUrl("https://www.antaranews.com/rss/ekonomi.xml", "Antara News", "EKONOMI") },
            async { dataSource.fetchFromRssUrl("https://www.antaranews.com/rss/olahraga.xml", "Antara News", "OLAHRAGA") },
            async { dataSource.fetchFromRssUrl("https://www.antaranews.com/rss/tekno.xml", "Antara News", "TEKNOLOGI") },

            // Sindonews
            async { dataSource.fetchFromRssUrl("https://nasional.sindonews.com/rss", "Sindonews", "NASIONAL") },
            async { dataSource.fetchFromRssUrl("https://ekbis.sindonews.com/rss", "Sindonews", "EKONOMI") },
            async { dataSource.fetchFromRssUrl("https://sports.sindonews.com/rss", "Sindonews", "OLAHRAGA") },
            async { dataSource.fetchFromRssUrl("https://lifestyle.sindonews.com/rss", "Sindonews", "HIBURAN") },

            // Republika
            async { dataSource.fetchFromRssUrl("https://www.republika.co.id/rss/nasional", "Republika", "NASIONAL") },
            async { dataSource.fetchFromRssUrl("https://www.republika.co.id/rss/ekonomi", "Republika", "EKONOMI") },
            async { dataSource.fetchFromRssUrl("https://www.republika.co.id/rss/sepakbola", "Republika", "OLAHRAGA") },

            // JPNN (Jawa Pos National Network)
            async { dataSource.fetchFromRssUrl("https://www.jpnn.com/index.php?mib=rss", "JPNN", "BERITA UTAMA") },

            // KapanLagi.com (Selebriti, Musik, Film & Gaya Hidup)
            async { dataSource.fetchFromRssUrl("https://www.kapanlagi.com/feed/", "KapanLagi.com", "HIBURAN") },

            // Okezone (MNC Media Network)
            async { dataSource.fetchFromRssUrl("https://sindikasi.okezone.com/index.php/rss/0/RSS2.0", "Okezone", "BERITA UTAMA") }
        )

        val remoteList = mutableListOf<NewsArticle>()
        for (deferred in feedDeferreds) {
            try {
                remoteList.addAll(deferred.await())
            } catch (_: Exception) { }
        }

        // Deduplicate by URL or title
        val distinct = remoteList.distinctBy { it.link.ifBlank { it.title } }

        if (distinct.isEmpty()) {
            dataSource.getInitialCuratedNews()
        } else {
            interleaveBySource(distinct)
        }
    }

    private fun interleaveBySource(articles: List<NewsArticle>): List<NewsArticle> {
        if (articles.isEmpty()) return emptyList()

        // Group by sourceName preserving natural newest-first order
        val groups = articles.groupBy { it.sourceName }
        val iterators = groups.values.map { it.iterator() }.toMutableList()
        val mixed = mutableListOf<NewsArticle>()

        while (iterators.isNotEmpty()) {
            val it = iterators.iterator()
            while (it.hasNext()) {
                val groupIter = it.next()
                if (groupIter.hasNext()) {
                    mixed.add(groupIter.next())
                } else {
                    it.remove()
                }
            }
        }
        return mixed
    }

    suspend fun toggleBookmark(article: NewsArticle, isCurrentlyBookmarked: Boolean) {
        if (isCurrentlyBookmarked) {
            bookmarkDao.deleteBookmarkByUrl(article.link)
        } else {
            bookmarkDao.insertBookmark(BookmarkEntity.fromArticle(article))
        }
    }

    suspend fun removeBookmark(url: String) {
        bookmarkDao.deleteBookmarkByUrl(url)
    }

    suspend fun clearAllBookmarks() {
        bookmarkDao.clearAllBookmarks()
    }
}
