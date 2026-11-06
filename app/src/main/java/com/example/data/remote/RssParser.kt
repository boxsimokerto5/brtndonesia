package com.example.data.remote

import android.util.Xml
import com.example.data.model.NewsArticle
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.util.UUID
import java.util.regex.Pattern

object RssParser {

    private val imgPattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>", Pattern.CASE_INSENSITIVE)
    private val htmlTagPattern = Pattern.compile("<[^>]+>")

    fun parse(inputStream: InputStream, sourceName: String, categoryName: String): List<NewsArticle> {
        val articles = mutableListOf<NewsArticle>()
        try {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            var currentArticleBuilder: ArticleBuilder? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name ?: ""

                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName.equals("item", ignoreCase = true) || tagName.equals("entry", ignoreCase = true)) {
                            currentArticleBuilder = ArticleBuilder()
                        } else if (currentArticleBuilder != null) {
                            when {
                                tagName.equals("title", ignoreCase = true) -> {
                                    currentArticleBuilder.title = safeNextText(parser)
                                }
                                tagName.equals("link", ignoreCase = true) -> {
                                    val href = parser.getAttributeValue(null, "href")
                                    if (!href.isNullOrBlank()) {
                                        currentArticleBuilder.link = href
                                    } else {
                                        val text = safeNextText(parser)
                                        if (text.isNotBlank()) {
                                            currentArticleBuilder.link = text
                                        }
                                    }
                                }
                                tagName.equals("description", ignoreCase = true) ||
                                tagName.equals("summary", ignoreCase = true) ||
                                tagName.equals("content", ignoreCase = true) -> {
                                    val rawDesc = safeNextText(parser)
                                    currentArticleBuilder.rawDescription = rawDesc
                                    if (currentArticleBuilder.imageUrl == null) {
                                        val extractedImg = extractImageFromHtml(rawDesc)
                                        if (extractedImg != null) {
                                            currentArticleBuilder.imageUrl = extractedImg
                                        }
                                    }
                                }
                                tagName.equals("pubDate", ignoreCase = true) ||
                                tagName.equals("published", ignoreCase = true) ||
                                tagName.equals("updated", ignoreCase = true) -> {
                                    currentArticleBuilder.pubDate = safeNextText(parser)
                                }
                                tagName.equals("enclosure", ignoreCase = true) -> {
                                    val type = parser.getAttributeValue(null, "type") ?: ""
                                    val url = parser.getAttributeValue(null, "url")
                                    if (url != null && (type.startsWith("image") || url.contains(".jpg") || url.contains(".png") || url.contains(".webp") || url.contains(".jpeg"))) {
                                        currentArticleBuilder.imageUrl = url
                                    }
                                }
                                tagName.contains("content", ignoreCase = true) || tagName.contains("thumbnail", ignoreCase = true) -> {
                                    val url = parser.getAttributeValue(null, "url")
                                    if (!url.isNullOrBlank()) {
                                        currentArticleBuilder.imageUrl = url
                                    }
                                }
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if ((tagName.equals("item", ignoreCase = true) || tagName.equals("entry", ignoreCase = true)) && currentArticleBuilder != null) {
                            val article = currentArticleBuilder.build(sourceName, categoryName)
                            if (article != null) {
                                articles.add(article)
                            }
                            currentArticleBuilder = null
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articles
    }

    private fun safeNextText(parser: XmlPullParser): String {
        return try {
            parser.nextText()
        } catch (_: Exception) {
            ""
        }
    }

    private fun extractImageFromHtml(html: String): String? {
        val matcher = imgPattern.matcher(html)
        if (matcher.find()) {
            return matcher.group(1)
        }
        return null
    }

    private fun cleanHtml(text: String): String {
        val noHtml = htmlTagPattern.matcher(text).replaceAll("")
        return noHtml
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#039;", "'")
            .replace("&apos;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&nbsp;", " ")
            .trim()
    }

    private class ArticleBuilder {
        var title: String = ""
        var link: String = ""
        var rawDescription: String = ""
        var pubDate: String = ""
        var imageUrl: String? = null

        fun build(sourceName: String, categoryName: String): NewsArticle? {
            val cleanTitle = cleanHtml(title)
            val cleanDesc = cleanHtml(rawDescription)
            val articleLink = link.trim()

            if (cleanTitle.isBlank() || articleLink.isBlank()) {
                return null
            }

            return NewsArticle(
                id = if (articleLink.isNotBlank()) articleLink else UUID.randomUUID().toString(),
                title = cleanTitle,
                link = articleLink,
                description = if (cleanDesc.isNotBlank()) cleanDesc else cleanTitle,
                pubDate = pubDate.trim(),
                imageUrl = imageUrl,
                sourceName = sourceName,
                category = categoryName
            )
        }
    }
}
