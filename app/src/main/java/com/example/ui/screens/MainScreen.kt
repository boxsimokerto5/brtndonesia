package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.DarkNusantaraBackground
import com.example.ui.theme.DarkNusantaraSurface
import com.example.ui.theme.NusantaraBlueActive
import com.example.ui.theme.TextPrimaryWhite
import com.example.ui.viewmodel.NewsViewModel
import com.example.util.CustomTabsHelper

enum class AppNavTab(val title: String) {
    FEED("Berita"),
    BOOKMARKS("Tersimpan"),
    SOURCES("Sumber")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(AppNavTab.FEED) }
    val darkToolbarColor = DarkNusantaraBackground.toArgb()

    // Handle user toast feedback
    LaunchedEffect(uiState.userToastMessage) {
        uiState.userToastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.consumeToast()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNusantaraBackground),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkNusantaraBackground,
                    titleContentColor = TextPrimaryWhite
                ),
                title = {
                    if (uiState.isSearchBarVisible) {
                        TextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            placeholder = { Text("Cari berita nusantara...", color = Color.Gray, fontSize = 15.sp) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = TextPrimaryWhite,
                                unfocusedTextColor = TextPrimaryWhite,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_text_field")
                        )
                    } else {
                        // Title exactly matching user's screenshot: "BERITA NUSANTARA"
                        Text(
                            text = "BERITA NUSANTARA",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            ),
                            color = TextPrimaryWhite,
                            modifier = Modifier.testTag("app_title_header")
                        )
                    }
                },
                actions = {
                    if (uiState.isSearchBarVisible) {
                        IconButton(
                            onClick = {
                                viewModel.toggleSearchBar()
                                focusManager.clearFocus()
                            },
                            modifier = Modifier.testTag("close_search_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextPrimaryWhite)
                        }
                    } else {
                        IconButton(
                            onClick = { viewModel.toggleSearchBar() },
                            modifier = Modifier.testTag("open_search_button")
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Cari berita",
                                tint = TextPrimaryWhite,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkNusantaraBackground,
                tonalElevation = 8.dp
            ) {
                // Tab 1: Berita
                NavigationBarItem(
                    selected = currentTab == AppNavTab.FEED,
                    onClick = { currentTab = AppNavTab.FEED },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppNavTab.FEED) Icons.Default.Newspaper else Icons.Outlined.Newspaper,
                            contentDescription = "Berita"
                        )
                    },
                    label = { Text("Berita") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = NusantaraBlueActive,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("tab_feed")
                )

                // Tab 2: Tersimpan
                NavigationBarItem(
                    selected = currentTab == AppNavTab.BOOKMARKS,
                    onClick = { currentTab = AppNavTab.BOOKMARKS },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (bookmarkedArticles.isNotEmpty()) {
                                    Badge(containerColor = NusantaraBlueActive) {
                                        Text(bookmarkedArticles.size.toString(), color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == AppNavTab.BOOKMARKS) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Tersimpan"
                            )
                        }
                    },
                    label = { Text("Tersimpan") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = NusantaraBlueActive,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("tab_bookmarks")
                )

                // Tab 3: Sumber & Info
                NavigationBarItem(
                    selected = currentTab == AppNavTab.SOURCES,
                    onClick = { currentTab = AppNavTab.SOURCES },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppNavTab.SOURCES) Icons.Default.Language else Icons.Outlined.Language,
                            contentDescription = "Sumber"
                        )
                    },
                    label = { Text("Sumber") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = NusantaraBlueActive,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    ),
                    modifier = Modifier.testTag("tab_sources")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkNusantaraBackground)
        ) {
            when (currentTab) {
                AppNavTab.FEED -> {
                    FeedScreen(
                        uiState = uiState,
                        onCategorySelect = { viewModel.selectCategory(it) },
                        onArticleClick = { article ->
                            CustomTabsHelper.openNewsArticle(context, article.link, darkToolbarColor)
                        },
                        onBookmarkClick = { article ->
                            viewModel.toggleBookmark(article)
                        },
                        onRefresh = { viewModel.loadNews(isRefresh = true) }
                    )
                }

                AppNavTab.BOOKMARKS -> {
                    BookmarksScreen(
                        bookmarkedArticles = bookmarkedArticles,
                        onArticleClick = { article ->
                            CustomTabsHelper.openNewsArticle(context, article.link, darkToolbarColor)
                        },
                        onRemoveBookmark = { article ->
                            viewModel.removeBookmark(article.link)
                        },
                        onShareClick = { article ->
                            CustomTabsHelper.shareArticle(context, article.title, article.link)
                        },
                        onClearAll = {
                            viewModel.clearAllBookmarks()
                        }
                    )
                }

                AppNavTab.SOURCES -> {
                    SourcesScreen(
                        onOpenPortalUrl = { url ->
                            CustomTabsHelper.openNewsArticle(context, url, darkToolbarColor)
                        }
                    )
                }
            }
        }
    }
}
