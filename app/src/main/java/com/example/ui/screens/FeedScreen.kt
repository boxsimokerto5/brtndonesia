package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NewsArticle
import com.example.data.model.NewsCategory
import com.example.ui.components.HeroCarouselPager
import com.example.ui.components.NewsCard
import com.example.ui.theme.DarkNusantaraBackground
import com.example.ui.theme.NusantaraBlueActive
import com.example.ui.theme.TextPrimaryWhite
import com.example.ui.theme.TextSecondaryMuted
import com.example.ui.viewmodel.NewsUiState

@Composable
fun FeedScreen(
    uiState: NewsUiState,
    onCategorySelect: (NewsCategory) -> Unit,
    onArticleClick: (NewsArticle) -> Unit,
    onBookmarkClick: (NewsArticle) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNusantaraBackground)
            .testTag("feed_screen")
    ) {
        // Active Filter Banner (if searching)
        AnimatedVisibility(visible = uiState.searchQuery.isNotBlank()) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Hasil pencarian: \"${uiState.searchQuery}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimaryWhite
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading && uiState.articles.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = NusantaraBlueActive)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Memuat kabar terkini Nusantara...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryMuted
                    )
                }
            } else if (uiState.articles.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = TextSecondaryMuted
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tidak Ada Berita",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Coba gunakan kata kunci lain atau pilih kategori berbeda.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryMuted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onRefresh,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NusantaraBlueActive),
                        modifier = Modifier.testTag("retry_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Segarkan Ulang Feed")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Top Carousel (Only when not in active text search)
                    if (uiState.searchQuery.isBlank() && uiState.featuredArticles.isNotEmpty()) {
                        item(key = "hero_carousel_section") {
                            HeroCarouselPager(
                                articles = uiState.featuredArticles,
                                onArticleClick = onArticleClick,
                                onBookmarkClick = onBookmarkClick
                            )
                        }
                    }

                    // Category Pill Tabs Row (matching screenshot: BERITA UTAMA, TEKNOLOGI, OLAHRAGA, etc.)
                    item(key = "categories_section") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NewsCategory.values().forEach { category ->
                                val isSelected = uiState.selectedCategory == category

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(
                                            if (isSelected) NusantaraBlueActive else Color.Transparent
                                        )
                                        .clickable { onCategorySelect(category) }
                                        .padding(horizontal = 16.dp, vertical = 9.dp)
                                        .testTag("category_pill_${category.name}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = category.displayName,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            letterSpacing = 0.5.sp
                                        ),
                                        color = if (isSelected) Color.White else TextPrimaryWhite.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }
                    }

                    // Feed Items (Split Cards)
                    items(
                        items = uiState.articles,
                        key = { it.link.ifBlank { it.title } }
                    ) { article ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            NewsCard(
                                article = article,
                                onArticleClick = onArticleClick,
                                onBookmarkClick = onBookmarkClick
                            )
                        }
                    }
                }
            }
        }
    }
}
