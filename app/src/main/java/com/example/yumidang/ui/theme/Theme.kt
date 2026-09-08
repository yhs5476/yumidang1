package com.example.yumidang.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = CoralPrimary,
    onPrimary = Color.White,
    primaryContainer = CoralPrimaryLight,
    onPrimaryContainer = CoralPrimaryDark,
    secondary = PeachSecondary,
    onSecondary = Color.White,
    secondaryContainer = PeachContainer,
    onSecondaryContainer = OnPeachContainer,
    background = WarmSand,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF3EFEA),
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
    error = SafetyRed
)

private val DarkColorScheme = darkColorScheme(
    primary = PeachSecondary,
    onPrimary = Color.White,
    primaryContainer = CoralPrimaryDark,
    onPrimaryContainer = Color.White,
    secondary = CoralPrimaryLight,
    onSecondary = Color.Black,
    background = Color(0xFF141416),
    onBackground = Color(0xFFEDEDED),
    surface = Color(0xFF1F2024),
    onSurface = Color(0xFFEDEDED),
    surfaceVariant = Color(0xFF2B2C31),
    onSurfaceVariant = Color(0xFFB0B2B8)
)

@Composable
fun YumidangTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
