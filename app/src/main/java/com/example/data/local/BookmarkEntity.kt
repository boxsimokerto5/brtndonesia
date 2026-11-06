package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.NewsArticle

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val url: String,
    val title: String,
    val description: String,
    val pubDate: String,
    val imageUrl: String?,
    val sourceName: String,
    val category: String,
    val savedTimestamp: Long = System.currentTimeMillis()
) {
    fun toNewsArticle(): NewsArticle {
        return NewsArticle(
            id = url,
            title = title,
            link = url,
            description = description,
            pubDate = pubDate,
            imageUrl = imageUrl,
            sourceName = sourceName,
            category = category,
            isBookmarked = true
        )
    }

    companion object {
        fun fromArticle(article: NewsArticle): BookmarkEntity {
            return BookmarkEntity(
                url = article.link,
                title = article.title,
                description = article.description,
                pubDate = article.pubDate,
                imageUrl = article.imageUrl,
                sourceName = article.sourceName,
                category = article.category,
                savedTimestamp = System.currentTimeMillis()
            )
        }
    }
}
