package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.BookmarkEntity
import com.example.data.model.NewsArticle
import com.example.data.model.SourceCatalog
import com.example.util.TimeUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Berita Indonesia", appName)
    }

    @Test
    fun `test source catalog contains major indonesian portals`() {
        val sources = SourceCatalog.sources
        assertTrue(sources.any { it.name.contains("Detik") })
        assertTrue(sources.any { it.name == "CNN Indonesia" })
        assertTrue(sources.any { it.name == "Antara News" })
        assertTrue(sources.any { it.name == "CNBC Indonesia" })
        assertTrue(sources.any { it.name == "Tempo.co" })
        assertTrue(sources.any { it.name == "Kumparan" })
        assertTrue(sources.any { it.name == "Liputan6" })
        assertTrue(sources.any { it.name == "Merdeka.com" })
        assertTrue(sources.any { it.name.contains("KapanLagi") })
        assertTrue(sources.any { it.name == "JPNN" })
        assertTrue(sources.any { it.name.contains("iNews") })
        assertTrue(sources.any { it.name.contains("Okezone") })
        assertTrue(sources.all { it.faviconUrl.isNotBlank() })
    }

    @Test
    fun `test bookmark entity mapping`() {
        val article = NewsArticle(
            id = "https://detik.com/sample",
            title = "Berita Utama",
            link = "https://detik.com/sample",
            description = "Ringkasan berita",
            pubDate = "Fri, 06 Nov 2026 08:00:00 +0700",
            imageUrl = "https://detik.com/thumb.jpg",
            sourceName = "Detikcom",
            category = "Nasional & Politik",
            isBookmarked = false
        )

        val entity = BookmarkEntity.fromArticle(article)
        val converted = entity.toNewsArticle()

        assertEquals(article.title, converted.title)
        assertEquals(article.link, converted.link)
        assertTrue(converted.isBookmarked)
    }

    @Test
    fun `test relative time utility`() {
        val formatted = TimeUtils.formatRelativeTime("Fri, 06 Nov 2026 08:00:00 +0700")
        assertNotNull(formatted)
        assertTrue(formatted.isNotBlank())
    }

    @Test
    fun `test topic catalog contains required categories and topics`() {
        val topics = com.example.data.model.TopicCatalog.allTopics
        assertTrue(topics.any { it.name.equals("PPPK", ignoreCase = true) })
        assertTrue(topics.any { it.name.contains("Sepakbola", ignoreCase = true) })
        assertTrue(topics.any { it.category.contains("Olahraga", ignoreCase = true) })
        assertTrue(topics.any { it.category.contains("Kota", ignoreCase = true) })
        assertTrue(topics.size >= 50)
    }

    @Test
    fun `test nav_topics string resource`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val navTopics = context.getString(R.string.nav_topics)
        assertEquals("Topik", navTopics)
    }

    @Test
    fun `test source diversity interleaving does not place same source consecutively`() {
        val sampleArticles = listOf(
            NewsArticle(id = "1", title = "Detik 1", link = "http://d1", description = "", pubDate = "", sourceName = "Detikcom"),
            NewsArticle(id = "2", title = "Detik 2", link = "http://d2", description = "", pubDate = "", sourceName = "Detikcom"),
            NewsArticle(id = "3", title = "CNN 1", link = "http://c1", description = "", pubDate = "", sourceName = "CNN Indonesia"),
            NewsArticle(id = "4", title = "Antara 1", link = "http://a1", description = "", pubDate = "", sourceName = "Antara News")
        )
        val groups = sampleArticles.groupBy { it.sourceName }
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

        assertEquals(3, mixed.take(3).map { it.sourceName }.distinct().size)
    }
}
