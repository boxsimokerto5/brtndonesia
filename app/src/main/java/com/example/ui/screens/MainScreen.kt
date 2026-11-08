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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
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
import com.example.ui.components.AboutAppDialog
import com.example.ui.components.OfficialWebsitesDrawerContent
import com.example.ui.components.PrivacyPolicyDialog
import com.example.ui.components.TermsOfServiceDialog
import com.example.ui.theme.NusantaraBlueActive
import com.example.ui.viewmodel.NewsViewModel
import com.example.util.CustomTabsHelper
import com.example.util.findActivity
import com.example.ad.AdManager
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
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showTermsOfServiceDialog by remember { mutableStateOf(false) }
    var showAboutAppDialog by remember { mutableStateOf(false) }
    var showOptionsMenu by remember { mutableStateOf(false) }
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
                    val activity = context.findActivity()
                    if (activity != null) {
                        AdManager.handleUserClick(activity) {
                            CustomTabsHelper.openNewsArticle(context, portal.siteUrl, dynamicToolbarColor)
                        }
                    } else {
                        CustomTabsHelper.openNewsArticle(context, portal.siteUrl, dynamicToolbarColor)
                    }
                    coroutineScope.launch { drawerState.close() }
                },
                onOpenPrivacyPolicy = {
                    coroutineScope.launch { drawerState.close() }
                    showPrivacyPolicyDialog = true
                },
                onOpenTermsOfService = {
                    coroutineScope.launch { drawerState.close() }
                    showTermsOfServiceDialog = true
                },
                onOpenAbout = {
                    coroutineScope.launch { drawerState.close() }
                    showAboutAppDialog = true
                },
                onShareApp = {
                    coroutineScope.launch { drawerState.close() }
                    CustomTabsHelper.shareApp(context)
                },
                onRateApp = {
                    coroutineScope.launch { drawerState.close() }
                    CustomTabsHelper.rateApp(context)
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

                        // Overflow Options Menu
                        Box {
                            IconButton(
                                onClick = { showOptionsMenu = !showOptionsMenu },
                                modifier = Modifier.testTag("overflow_menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Pilihan Lainnya",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }

                            DropdownMenu(
                                expanded = showOptionsMenu,
                                onDismissRequest = { showOptionsMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Tentang Aplikasi") },
                                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = NusantaraBlueActive) },
                                    onClick = {
                                        showOptionsMenu = false
                                        showAboutAppDialog = true
                                    },
                                    modifier = Modifier.testTag("menu_about")
                                )
                                DropdownMenuItem(
                                    text = { Text("Bagikan Aplikasi") },
                                    leadingIcon = { Icon(Icons.Default.Share, contentDescription = null, tint = NusantaraBlueActive) },
                                    onClick = {
                                        showOptionsMenu = false
                                        CustomTabsHelper.shareApp(context)
                                    },
                                    modifier = Modifier.testTag("menu_share")
                                )
                                DropdownMenuItem(
                                    text = { Text("Nilai di Play Store") },
                                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300)) },
                                    onClick = {
                                        showOptionsMenu = false
                                        CustomTabsHelper.rateApp(context)
                                    },
                                    modifier = Modifier.testTag("menu_rate")
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Kebijakan Privasi") },
                                    leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF2E7D32)) },
                                    onClick = {
                                        showOptionsMenu = false
                                        showPrivacyPolicyDialog = true
                                    },
                                    modifier = Modifier.testTag("menu_privacy")
                                )
                                DropdownMenuItem(
                                    text = { Text("Syarat & Ketentuan") },
                                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFFC62828)) },
                                    onClick = {
                                        showOptionsMenu = false
                                        showTermsOfServiceDialog = true
                                    },
                                    modifier = Modifier.testTag("menu_terms")
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
                                val activity = context.findActivity()
                                if (activity != null) {
                                    AdManager.handleUserClick(activity) {
                                        CustomTabsHelper.openNewsArticle(context, article.link, dynamicToolbarColor)
                                    }
                                } else {
                                    CustomTabsHelper.openNewsArticle(context, article.link, dynamicToolbarColor)
                                }
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
                                val activity = context.findActivity()
                                if (activity != null) {
                                    AdManager.handleUserClick(activity) {
                                        CustomTabsHelper.openNewsArticle(context, article.link, dynamicToolbarColor)
                                    }
                                } else {
                                    CustomTabsHelper.openNewsArticle(context, article.link, dynamicToolbarColor)
                                }
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
                                val activity = context.findActivity()
                                if (activity != null) {
                                    AdManager.handleUserClick(activity) {
                                        viewModel.searchTopic(topicKeyword)
                                        currentTab = AppNavTab.FEED
                                    }
                                } else {
                                    viewModel.searchTopic(topicKeyword)
                                    currentTab = AppNavTab.FEED
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Modal Dialogs for Legal and App Info
    if (showPrivacyPolicyDialog) {
        PrivacyPolicyDialog(
            onDismiss = { showPrivacyPolicyDialog = false }
        )
    }

    if (showTermsOfServiceDialog) {
        TermsOfServiceDialog(
            onDismiss = { showTermsOfServiceDialog = false }
        )
    }

    if (showAboutAppDialog) {
        AboutAppDialog(
            onDismiss = { showAboutAppDialog = false },
            onShareApp = {
                CustomTabsHelper.shareApp(context)
            },
            onRateApp = {
                CustomTabsHelper.rateApp(context)
            }
        )
    }
}
