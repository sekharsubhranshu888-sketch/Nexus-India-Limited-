package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NexusColorScheme = darkColorScheme(
    primary = NexusCyan,
    onPrimary = NexusObsidian,
    primaryContainer = NexusSlateCard,
    onPrimaryContainer = NexusCyanLight,
    secondary = NexusViolet,
    onSecondary = NexusObsidian,
    secondaryContainer = NexusSlateCard,
    onSecondaryContainer = NexusVioletLight,
    tertiary = NexusEmerald,
    onTertiary = NexusObsidian,
    tertiaryContainer = NexusSlateDark,
    onTertiaryContainer = NexusEmeraldLight,
    background = NexusObsidian,
    onBackground = NexusTextPrimary,
    surface = NexusSlateDark,
    onSurface = NexusTextPrimary,
    surfaceVariant = NexusSlateCard,
    onSurfaceVariant = NexusTextSecondary,
    outline = NexusCardBorder,
    error = NexusRose,
    onError = NexusObsidian
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to futuristic stealth dark mode
    dynamicColor: Boolean = false, // Keep Nexus branding consistent
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = NexusObsidian.toArgb()
                window.navigationBarColor = NexusObsidian.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = NexusColorScheme,
        typography = Typography,
        content = content
    )
}
