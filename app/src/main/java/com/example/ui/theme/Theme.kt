package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NusantaraBlueActive,
    onPrimary = NusantaraBlueOn,
    primaryContainer = NusantaraBlueActive.copy(alpha = 0.25f),
    onPrimaryContainer = Color.White,
    secondary = NusantaraBlueActive,
    onSecondary = Color.White,
    background = DarkNusantaraBackground,
    surface = DarkNusantaraSurface,
    surfaceVariant = DarkNusantaraSurfaceVariant,
    onBackground = TextPrimaryWhite,
    onSurface = TextPrimaryWhite,
    onSurfaceVariant = TextSecondaryMuted,
    outline = DarkNusantaraSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = NusantaraBlueActive,
    onPrimary = Color.White,
    primaryContainer = NusantaraBlueActive.copy(alpha = 0.15f),
    onPrimaryContainer = NusantaraBlueActive,
    background = LightNusantaraBackground,
    surface = LightNusantaraSurface,
    surfaceVariant = LightNusantaraSurfaceVariant,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFF5A6672)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark theme to match user's screenshot layout
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
