package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.NewsRepository
import com.example.ui.screens.MainScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.NewsViewModel
import com.example.ui.viewmodel.NewsViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = NewsRepository(database.bookmarkDao())
        val themePreferences = com.example.util.ThemePreferences.getInstance(applicationContext)
        val viewModelFactory = NewsViewModelFactory(repository, themePreferences)

        // Initialize TopOn Ad Mediation & Meta Audience Network safely
        try {
            com.example.ad.AdManager.init(this)
        } catch (t: Throwable) {
            android.util.Log.e("MainActivity", "AdManager init failed gracefully", t)
        }

        setContent {
            val viewModel: NewsViewModel = viewModel(factory = viewModelFactory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            var isSplashActive by remember { mutableStateOf(true) }

            MyApplicationTheme(darkTheme = uiState.isDarkMode) {
                Crossfade(
                    targetState = isSplashActive,
                    animationSpec = tween(durationMillis = 500),
                    label = "splash_crossfade"
                ) { showSplash ->
                    if (showSplash) {
                        SplashScreen(
                            onSplashFinished = { isSplashActive = false }
                        )
                    } else {
                        MainScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
