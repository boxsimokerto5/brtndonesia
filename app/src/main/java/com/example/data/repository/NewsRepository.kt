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
        // Concurrently fetch across premier Indonesian sources
        val detikDeferred = async { dataSource.fetchFromJsonApi("detik", "terbaru", "Detikcom", "Terkini") }
        val cnnDeferred = async { dataSource.fetchFromJsonApi("cnn", "terbaru", "CNN Indonesia", "Terkini") }
        val antaraDeferred = async { dataSource.fetchFromRssUrl("https://www.antaranews.com/rss/terkini.xml", "Antara News", "Nasional & Politik") }
        val cnbcDeferred = async { dataSource.fetchFromRssUrl("https://www.cnbcindonesia.com/news/rss", "CNBC Indonesia", "Ekonomi & Bisnis") }
        val tempoDeferred = async { dataSource.fetchFromRssUrl("https://rss.tempo.co/nasional", "Tempo.co", "Nasional & Politik") }
        val republikaDeferred = async { dataSource.fetchFromRssUrl("https://republika.co.id/rss", "Republika", "Terkini") }
        val suaraDeferred = async { dataSource.fetchFromRssUrl("https://www.suara.com/rss", "Suara.com", "Terkini") }

        val remoteList = mutableListOf<NewsArticle>()
        val results = listOf(
            detikDeferred.await(),
            cnnDeferred.await(),
            antaraDeferred.await(),
            cnbcDeferred.await(),
            tempoDeferred.await(),
            republikaDeferred.await(),
            suaraDeferred.await()
        )

        for (list in results) {
            remoteList.addAll(list)
        }

        // Deduplicate by URL or title
        val distinct = remoteList.distinctBy { it.link.ifBlank { it.title } }

        if (distinct.isEmpty()) {
            dataSource.getInitialCuratedNews()
        } else {
            distinct
        }
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
