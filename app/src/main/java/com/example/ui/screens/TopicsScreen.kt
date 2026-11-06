package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TopicCatalog
import com.example.data.model.TopicItem
import com.example.ui.theme.NusantaraBlueActive

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopicsScreen(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("Semua") }
    var filterQuery by remember { mutableStateOf("") }

    val filteredTopics by remember(selectedCategory, filterQuery) {
        derivedStateOf {
            val baseList = if (selectedCategory == "Semua") {
                TopicCatalog.allTopics
            } else {
                TopicCatalog.allTopics.filter { it.category == selectedCategory }
            }

            if (filterQuery.isBlank()) {
                baseList
            } else {
                val q = filterQuery.trim().lowercase()
                baseList.filter {
                    it.name.lowercase().contains(q) ||
                    it.category.lowercase().contains(q) ||
                    it.searchKeyword.lowercase().contains(q)
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("topics_screen"),
        contentPadding = PaddingValues(bottom = 88.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = NusantaraBlueActive,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Tag,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Kumpulan Topik Berita",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Jelajahi ${TopicCatalog.allTopics.size}+ topik populer & trending",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Tekan tombol kata apa pun di bawah untuk langsung menemukan semua berita terkini dari media terpercaya (Detik, CNN, Antara, Kompas, CNBC, dll).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Internal Quick Search Input
                    OutlinedTextField(
                        value = filterQuery,
                        onValueChange = { filterQuery = it },
                        placeholder = { Text("Saring topik (misal: pppk, bola, kota)...", fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = NusantaraBlueActive,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (filterQuery.isNotEmpty()) {
                                IconButton(onClick = { filterQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Hapus teks",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NusantaraBlueActive,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("topics_filter_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Random Pick Button
                    FilledTonalButton(
                        onClick = {
                            val randomTopic = TopicCatalog.allTopics.random()
                            onTopicClick(randomTopic.searchKeyword)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = NusantaraBlueActive.copy(alpha = 0.12f),
                            contentColor = NusantaraBlueActive
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("random_topic_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pilihkan Topik Acak Untuk Saya 🎲",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Category Filter Chips Carousel
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(TopicCatalog.categories) { categoryName ->
                    val isSelected = selectedCategory == categoryName
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = categoryName },
                        label = {
                            Text(
                                text = categoryName,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NusantaraBlueActive,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            selectedBorderColor = NusantaraBlueActive
                        ),
                        modifier = Modifier.testTag("category_chip_$categoryName")
                    )
                }
            }
        }

        // Topics Count & Section Label
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedCategory == "Semua") "Semua Kata Kunci Populer" else "Topik $selectedCategory",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NusantaraBlueActive.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${filteredTopics.size} Kata",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = NusantaraBlueActive,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Flow Layout of scattered word buttons
        item {
            if (filteredTopics.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada topik yang cocok dengan \"$filterQuery\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    filteredTopics.forEach { topic ->
                        TopicWordChip(
                            topic = topic,
                            onClick = { onTopicClick(topic.searchKeyword) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopicWordChip(
    topic: TopicItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = if (topic.isHot) 3.dp else 1.dp,
        shadowElevation = if (topic.isHot) 2.dp else 0.dp,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (topic.isHot) 1.5.dp else 1.dp,
                color = if (topic.isHot) NusantaraBlueActive.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .testTag("topic_chip_${topic.name}")
    ) {
        Row(
            modifier = Modifier
                .defaultMinSize(minHeight = 44.dp)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji Icon
            Text(
                text = topic.iconEmoji,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Topic Name
            Text(
                text = topic.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (topic.isHot) FontWeight.Bold else FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (topic.isHot) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFE53935).copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "HOT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFE53935)
                        )
                    }
                }
            }
        }
    }
}
