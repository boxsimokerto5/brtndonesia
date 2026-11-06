package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.model.NewsArticle
import com.example.ui.components.NewsCard
import com.example.ui.theme.DarkNusantaraBackground
import com.example.ui.theme.NusantaraBlueActive
import com.example.ui.theme.TextPrimaryWhite
import com.example.ui.theme.TextSecondaryMuted

@Composable
fun BookmarksScreen(
    bookmarkedArticles: List<NewsArticle>,
    onArticleClick: (NewsArticle) -> Unit,
    onRemoveBookmark: (NewsArticle) -> Unit,
    onShareClick: (NewsArticle) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Hapus Semua Simpanan?", color = TextPrimaryWhite) },
            text = { Text("Apakah Anda yakin ingin menghapus semua artikel yang telah disimpan dari perangkat?", color = TextSecondaryMuted) },
            containerColor = MaterialTheme.colorScheme.surface,
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearAll()
                        showClearDialog = false
                    }
                ) {
                    Text("Hapus Semua", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Batal", color = TextPrimaryWhite)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNusantaraBackground)
            .testTag("bookmarks_screen")
    ) {
        // Header Row with count and clear button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Artikel Tersimpan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryWhite
                )
                Text(
                    text = "${bookmarkedArticles.size} artikel siap dibaca",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryMuted
                )
            }

            if (bookmarkedArticles.isNotEmpty()) {
                FilledTonalButton(
                    onClick = { showClearDialog = true },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.testTag("clear_all_bookmarks_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Bersihkan", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)

        if (bookmarkedArticles.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = NusantaraBlueActive.copy(alpha = 0.2f),
                    modifier = Modifier.size(80.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = NusantaraBlueActive
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Belum Ada Artikel Tersimpan",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryWhite
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tekan ikon bookmark pada artikel mana pun di feed untuk menyimpannya ke daftar bacaan offline ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryMuted,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = bookmarkedArticles,
                    key = { it.link }
                ) { article ->
                    NewsCard(
                        article = article,
                        onArticleClick = onArticleClick,
                        onBookmarkClick = onRemoveBookmark
                    )
                }
            }
        }
    }
}
