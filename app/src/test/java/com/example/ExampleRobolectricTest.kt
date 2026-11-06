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
}
