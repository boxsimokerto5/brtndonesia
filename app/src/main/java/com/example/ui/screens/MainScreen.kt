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
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Tag
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
import com.example.ui.components.OfficialWebsitesDrawerContent
import com.example.ui.theme.NusantaraBlueActive
import com.example.ui.viewmodel.NewsViewModel
import com.example.util.CustomTabsHelper
import kotlinx.coroutines.launch

enum class AppNavTab(val title: String) {
    FEED("Berita"),
    BOOKMARKS("Tersimpan"),
    TOPICS("Topik")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bookmarkedArticles by viewModel.bookmarkedArticles.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(AppNavTab.FEED) }
    val dynamicToolbarColor = MaterialTheme.colorScheme.surface.toArgb()

    // Handle user toast feedback
    LaunchedEffect(uiState.userToastMessage) {
        uiState.userToastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.consumeToast()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            OfficialWebsitesDrawerContent(
                onSelectPortal = { portal ->
                    CustomTabsHelper.openNewsArticle(context, portal.siteUrl, dynamicToolbarColor)
                    coroutineScope.launch { drawerState.close() }
                },
                onCloseDrawer = {
                    coroutineScope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            },
                            modifier = Modifier.testTag("burger_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu portal berita",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    },
                    title = {
                        if (uiState.isSearchBarVisible) {
                            TextField(
                                value = uiState.searchQuery,
                                onValueChange = { viewModel.onSearchQueryChanged(it) },
                                placeholder = { Text("Cari berita indonesia...", fontSize = 15.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
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
                            // Title: "BERITA INDONESIA"
                            Text(
                                text = "BERITA INDONESIA",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                modifier = Modifier.testTag("app_title_header")
                            )
                        }
                    },
                    actions = {
                        // Theme Toggle Button (Light/Dark mode)
                        IconButton(
                            onClick = { viewModel.toggleDarkMode() },
                            modifier = Modifier.testTag("toggle_theme_button")
                        ) {
                            Icon(
                                imageVector = if (uiState.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = if (uiState.isDarkMode) "Beralih ke Mode Terang" else "Beralih ke Mode Gelap",
                                tint = if (uiState.isDarkMode) Color(0xFFFFD54F) else MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        if (uiState.isSearchBarVisible) {
                            IconButton(
                                onClick = {
                                    viewModel.toggleSearchBar()
                                    focusManager.clearFocus()
                                },
                                modifier = Modifier.testTag("close_search_button")
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Tutup", tint = MaterialTheme.colorScheme.onBackground)
                            }
                        } else {
                            IconButton(
                                onClick = { viewModel.toggleSearchBar() },
                                modifier = Modifier.testTag("open_search_button")
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Cari berita",
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
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
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = NusantaraBlueActive,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
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
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = NusantaraBlueActive,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.testTag("tab_bookmarks")
                    )

                    // Tab 3: Topik
                    NavigationBarItem(
                        selected = currentTab == AppNavTab.TOPICS,
                        onClick = { currentTab = AppNavTab.TOPICS },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == AppNavTab.TOPICS) Icons.Default.Tag else Icons.Outlined.Tag,
                                contentDescription = "Topik"
                            )
                        },
                        label = { Text("Topik") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = NusantaraBlueActive,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.testTag("tab_topics")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                when (currentTab) {
                    AppNavTab.FEED -> {
                        FeedScreen(
                            uiState = uiState,
                            onCategorySelect = { viewModel.selectCategory(it) },
                            onArticleClick = { article ->
                                CustomTabsHelper.openNewsArticle(context, article.link, dynamicToolbarColor)
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
                                CustomTabsHelper.openNewsArticle(context, article.link, dynamicToolbarColor)
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

                    AppNavTab.TOPICS -> {
                        TopicsScreen(
                            onTopicClick = { topicKeyword ->
                                viewModel.searchTopic(topicKeyword)
                                currentTab = AppNavTab.FEED
                            }
                        )
                    }
                }
            }
        }
    }
}
