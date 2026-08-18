package com.wealthmetric.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = IndigoAccent,
    onPrimary = DarkButtonText,
    primaryContainer = IndigoContainer,
    onPrimaryContainer = IndigoDark,
    secondary = EmeraldPrimary,
    onSecondary = DarkButtonText,
    secondaryContainer = EmeraldContainer,
    onSecondaryContainer = EmeraldDark,
    tertiary = CyanAccent,
    background = AppBackground,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = CardSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

@Composable
fun WealthMetricTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
