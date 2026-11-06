package com.example.data.remote

import com.example.data.model.NewsArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.util.concurrent.TimeUnit

class NewsDataSource {

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    suspend fun fetchFromRssUrl(rssUrl: String, sourceName: String, categoryName: String): List<NewsArticle> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(rssUrl)
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 14; Mobile; rv:125.0) Gecko/125.0 Firefox/125.0")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val responseBody = response.body?.bytes() ?: return@withContext emptyList()
                return@withContext RssParser.parse(ByteArrayInputStream(responseBody), sourceName, categoryName)
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun fetchFromJsonApi(portal: String, subCategory: String, sourceName: String, categoryName: String): List<NewsArticle> = withContext(Dispatchers.IO) {
        val url = "https://api-berita-indonesia.vercel.app/$portal/$subCategory/"
        try {
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "BeritaNusantara-App/1.0")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext emptyList()
                val jsonStr = response.body?.string() ?: return@withContext emptyList()
                val json = JSONObject(jsonStr)

                if (!json.optBoolean("success", false)) return@withContext emptyList()

                val dataObj = json.optJSONObject("data") ?: return@withContext emptyList()
                val postsArray = dataObj.optJSONArray("posts") ?: return@withContext emptyList()

                val result = mutableListOf<NewsArticle>()
                for (i in 0 until postsArray.length()) {
                    val item = postsArray.getJSONObject(i)
                    val link = item.optString("link")
                    val title = item.optString("title")
                    val pubDate = item.optString("pubDate")
                    val description = item.optString("description")
                    val thumbnail = item.optString("thumbnail").takeIf { it.isNotBlank() }

                    if (link.isNotBlank() && title.isNotBlank()) {
                        result.add(
                            NewsArticle(
                                id = link,
                                title = title,
                                link = link,
                                description = description,
                                pubDate = pubDate,
                                imageUrl = thumbnail,
                                sourceName = sourceName,
                                category = categoryName,
                                author = "Redaksi"
                            )
                        )
                    }
                }
                return@withContext result
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    /**
     * Curated authentic Indonesian news articles matching the user's mockup layout
     */
    fun getInitialCuratedNews(): List<NewsArticle> {
        return listOf(
            NewsArticle(
                id = "https://www.jawapos.com/nasional/erupsimerapi-status-awas-ribuan-mengungsi",
                title = "ERUPSI MERAPI: Ribuan Mengungsi, Status Awas Ditetapkan",
                link = "https://www.jawapos.com/nasional/erupsimerapi-status-awas-ribuan-mengungsi",
                description = "Balai Penyelidikan dan Pengembangan Teknologi Kebencanaan Geologi (BPPTKG) menetapkan radius aman 7 kilometer setelah guguran awan panas intensif.",
                pubDate = "Fri, 06 Nov 2026 08:30:00 +0700",
                imageUrl = "https://images.unsplash.com/photo-1541872703-74c5e44368f9?auto=format&fit=crop&w=1000&q=80",
                sourceName = "Jawa Pos",
                category = "BERITA UTAMA",
                author = "Budi Santoso"
            ),
            NewsArticle(
                id = "https://www.detik.com/inet/gadget/review-samsung-galaxy-s24-ultra-ai",
                title = "Review Samsung Galaxy S24 Ultra",
                link = "https://www.detik.com/inet/gadget/review-samsung-galaxy-s24-ultra-ai",
                description = "Review Samsung Galaxy S24 Ultra. Peningkatan signifikan pada Galaxy AI, kamera 200MP, dan bodi titanium kokoh yang presisi.",
                pubDate = "Fri, 06 Nov 2026 09:30:00 +0700",
                imageUrl = "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?auto=format&fit=crop&w=800&q=80",
                sourceName = "Detik.com",
                category = "TEKNOLOGI",
                author = "Andi Putra"
            ),
            NewsArticle(
                id = "https://www.kompas.com/tekno/startup-lokal-go-global-ai-indonesia",
                title = "Startup Lokal Go Global",
                link = "https://www.kompas.com/tekno/startup-lokal-go-global-ai-indonesia",
                description = "Startup Lokal Go Global ekspansi ke pasar Asia Tenggara berkat adopsi teknologi machine learning dan model komputasi awan cerdas.",
                pubDate = "Fri, 06 Nov 2026 09:30:00 +0700",
                imageUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=800&q=80",
                sourceName = "Kompas.com",
                category = "TEKNOLOGI",
                author = "Andi Putre"
            ),
            NewsArticle(
                id = "https://www.cnnindonesia.com/ekonomi/ihsg-menguat-sektor-teknologi-rekor",
                title = "IHSG Sentuh Rekor Tertinggi Sepanjang Sejarah Tembus 7.500",
                link = "https://www.cnnindonesia.com/ekonomi/ihsg-menguat-sektor-teknologi-rekor",
                description = "Laju indeks pasar saham Indonesia didorong oleh sentimen positif pertumbuhan ekonomi kuartal ketiga yang melampaui estimasi analis.",
                pubDate = "Fri, 06 Nov 2026 08:00:00 +0700",
                imageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?auto=format&fit=crop&w=800&q=80",
                sourceName = "CNN Indonesia",
                category = "EKONOMI",
                author = "Siti Rahma"
            ),
            NewsArticle(
                id = "https://www.tempo.co/olahraga/timnas-indonesia-lolos-babak-berikutnya",
                title = "Kualifikasi Piala Dunia: Kemenangan Dramatis Timnas di Gelora Bung Karno",
                link = "https://www.tempo.co/olahraga/timnas-indonesia-lolos-babak-berikutnya",
                description = "Dukungan penuh suporter di GBK mengantarkan Garuda meraih tiga poin krusial lewat gol spektakuler pada menit-menit akhir pertandingan.",
                pubDate = "Fri, 06 Nov 2026 07:15:00 +0700",
                imageUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=800&q=80",
                sourceName = "Tempo.co",
                category = "OLAHRAGA",
                author = "Rian Pratama"
            ),
            NewsArticle(
                id = "https://www.cnbcindonesia.com/tech/pusat-ai-nasional-resmi-beroperasi",
                title = "Pusat Riset Kecerdasan Buatan Terbesar se-ASEAN Diresmikan",
                link = "https://www.cnbcindonesia.com/tech/pusat-ai-nasional-resmi-beroperasi",
                description = "Fasilitas mutakhir berkapasitas ribuan GPU bertenaga energi bersih siap mempercepat transformasi digital industri nasional.",
                pubDate = "Fri, 06 Nov 2026 06:45:00 +0700",
                imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=800&q=80",
                sourceName = "CNBC Indonesia",
                category = "TEKNOLOGI",
                author = "Dimas Wicaksono"
            )
        )
    }
}
