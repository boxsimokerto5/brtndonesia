package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.NewsArticle
import com.example.data.model.NewsCategory
import com.example.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NewsUiState(
    val articles: List<NewsArticle> = emptyList(),
    val featuredArticles: List<NewsArticle> = emptyList(),
    val selectedCategory: NewsCategory = NewsCategory.BERITA_UTAMA,
    val selectedSource: String? = null,
    val searchQuery: String = "",
    val isSearchBarVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val userToastMessage: String? = null,
    val isDarkMode: Boolean = false // Standar awal saat membuka pertama kali adalah tampilan terang
)

class NewsViewModel(
    private val repository: NewsRepository,
    private val themePreferences: com.example.util.ThemePreferences? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        NewsUiState(
            isLoading = true,
            isDarkMode = themePreferences?.isDarkMode() ?: false
        )
    )
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private var rawArticles: List<NewsArticle> = emptyList()
    private var bookmarkedUrls: Set<String> = emptySet()

    val bookmarkedArticles: StateFlow<List<NewsArticle>> = repository.bookmarkedArticles
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Observe bookmarked URLs reactively
        viewModelScope.launch {
            repository.bookmarkedUrls.collect { urls ->
                bookmarkedUrls = urls
                recomputeArticles()
            }
        }
        loadNews(isRefresh = false)
    }

    fun loadNews(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = !isRefresh,
                isRefreshing = isRefresh,
                errorMessage = null
            )

            try {
                val fetched = repository.fetchAllLatestNews()
                rawArticles = fetched
                recomputeArticles()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    errorMessage = if (fetched.isEmpty()) "Tidak ada berita yang dapat dimuat saat ini." else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    errorMessage = "Gagal memuat feed berita: ${e.localizedMessage ?: "Koneksi terputus"}"
                )
            }
        }
    }

    fun selectCategory(category: NewsCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        recomputeArticles()
    }

    fun selectSource(sourceName: String?) {
        val nextSource = if (_uiState.value.selectedSource == sourceName) null else sourceName
        _uiState.value = _uiState.value.copy(selectedSource = nextSource)
        recomputeArticles()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        recomputeArticles()
    }

    fun searchTopic(topicKeyword: String) {
        _uiState.value = _uiState.value.copy(
            selectedCategory = NewsCategory.BERITA_UTAMA,
            selectedSource = null,
            searchQuery = topicKeyword,
            isSearchBarVisible = true,
            userToastMessage = "Menampilkan topik: $topicKeyword"
        )
        recomputeArticles()
    }

    fun toggleSearchBar() {
        val willShow = !_uiState.value.isSearchBarVisible
        _uiState.value = _uiState.value.copy(
            isSearchBarVisible = willShow,
            searchQuery = if (!willShow) "" else _uiState.value.searchQuery
        )
        recomputeArticles()
    }

    fun toggleBookmark(article: NewsArticle) {
        viewModelScope.launch {
            val isCurrent = article.isBookmarked
            repository.toggleBookmark(article, isCurrent)
            _uiState.value = _uiState.value.copy(
                userToastMessage = if (!isCurrent) "Artikel berhasil disimpan" else "Artikel dihapus dari simpanan"
            )
        }
    }

    fun removeBookmark(url: String) {
        viewModelScope.launch {
            repository.removeBookmark(url)
            _uiState.value = _uiState.value.copy(userToastMessage = "Artikel dihapus dari simpanan")
        }
    }

    fun clearAllBookmarks() {
        viewModelScope.launch {
            repository.clearAllBookmarks()
            _uiState.value = _uiState.value.copy(userToastMessage = "Semua simpanan artikel telah dibersihkan")
        }
    }

    fun consumeToast() {
        _uiState.value = _uiState.value.copy(userToastMessage = null)
    }

    fun toggleDarkMode() {
        val nextMode = !_uiState.value.isDarkMode
        themePreferences?.setDarkMode(nextMode)
        _uiState.value = _uiState.value.copy(
            isDarkMode = nextMode,
            userToastMessage = if (nextMode) "Mode gelap aktif" else "Mode terang aktif"
        )
    }

    fun setDarkMode(enabled: Boolean) {
        if (_uiState.value.isDarkMode != enabled) {
            themePreferences?.setDarkMode(enabled)
            _uiState.value = _uiState.value.copy(
                isDarkMode = enabled,
                userToastMessage = if (enabled) "Mode gelap aktif" else "Mode terang aktif"
            )
        }
    }

    private fun recomputeArticles() {
        val currentState = _uiState.value
        val category = currentState.selectedCategory
        val source = currentState.selectedSource
        val query = currentState.searchQuery

        // Map bookmark status dynamically
        val articlesWithBookmarks = rawArticles.map { article ->
            article.copy(isBookmarked = bookmarkedUrls.contains(article.link))
        }

        // Filter by category
        val categoryFiltered = if (category == NewsCategory.BERITA_UTAMA) {
            articlesWithBookmarks
        } else {
            articlesWithBookmarks.filter { article ->
                article.category.contains(category.displayName, ignoreCase = true) ||
                article.title.contains(category.displayName, ignoreCase = true) ||
                (category == NewsCategory.TEKNOLOGI && (article.title.contains("teknologi", true) || article.title.contains("AI", true) || article.title.contains("gadget", true) || article.title.contains("internet", true) || article.title.contains("samsung", true) || article.title.contains("startup", true))) ||
                (category == NewsCategory.EKONOMI && (article.title.contains("ekonomi", true) || article.title.contains("ihsg", true) || article.title.contains("bisnis", true) || article.title.contains("rupiah", true) || article.title.contains("pasar", true))) ||
                (category == NewsCategory.OLAHRAGA && (article.title.contains("olahraga", true) || article.title.contains("timnas", true) || article.title.contains("bola", true) || article.title.contains("juara", true))) ||
                (category == NewsCategory.HIBURAN && (article.title.contains("kuliner", true) || article.title.contains("artis", true) || article.title.contains("film", true) || article.title.contains("gaya", true)))
            }
        }

        // Filter by source
        val sourceFiltered = if (source.isNullOrBlank()) {
            categoryFiltered
        } else {
            categoryFiltered.filter { it.sourceName.contains(source, ignoreCase = true) }
        }

        // Filter by search query
        val finalArticles = if (query.isBlank()) {
            sourceFiltered
        } else {
            val q = query.trim().lowercase()
            sourceFiltered.filter {
                it.title.lowercase().contains(q) || it.description.lowercase().contains(q) || it.sourceName.lowercase().contains(q)
            }
        }

        val mixedArticles = interleaveBySource(finalArticles)

        // Keywords for ranking top trending news (PPPK, Timnas, Sepakbola, IKN, Emas, etc.)
        val trendingKeywords = listOf(
            "trending", "viral", "pppk", "cpns", "timnas", "shin tae-yong", "sepakbola",
            "ikn", "pilkada", "emas", "rupiah", "gempa", "bmkg", "ihsg", "liga",
            "pertamina", "bbm", "ai", "apple", "iphone", "juara", "kpk", "bansos", "presiden"
        )

        // Select articles with images, sorted by trending keyword relevance
        val articlesWithImages = articlesWithBookmarks.filter { !it.imageUrl.isNullOrBlank() }
        val trendingRanked = articlesWithImages.sortedByDescending { article ->
            val text = (article.title + " " + article.description).lowercase()
            trendingKeywords.count { kw -> text.contains(kw) }
        }

        // Diversify Top 5 Trending across distinct publishers so multiple providers appear
        val distinctBySource = trendingRanked.distinctBy { it.sourceName }.take(5)
        val featured = if (distinctBySource.size >= 5) {
            distinctBySource
        } else {
            val needed = 5 - distinctBySource.size
            val remainder = trendingRanked.filter { r -> distinctBySource.none { it.link == r.link } }.take(needed)
            distinctBySource + remainder
        }

        _uiState.value = currentState.copy(
            articles = mixedArticles,
            featuredArticles = featured
        )
    }

    private fun interleaveBySource(articles: List<NewsArticle>): List<NewsArticle> {
        if (articles.isEmpty()) return emptyList()
        val groups = articles.groupBy { it.sourceName }
        if (groups.size <= 1) return articles

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
}

class NewsViewModelFactory(
    private val repository: NewsRepository,
    private val themePreferences: com.example.util.ThemePreferences? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(repository, themePreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
