package com.example.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreen80,
    onPrimary = Color(0xFF00391F),
    primaryContainer = EmeraldGreenDark,
    onPrimaryContainer = EmeraldGreen80,
    secondary = GoldenAmber80,
    onSecondary = Color(0xFF452B00),
    secondaryContainer = Color(0xFF634000),
    onSecondaryContainer = GoldenAmber80,
    tertiary = SageGreen80,
    background = Color(0xFF111814),
    surface = Color(0xFF19231D),
    onBackground = Color(0xFFE2EBE5),
    onSurface = Color(0xFFE2EBE5)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldGreen40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD4EEDC),
    onPrimaryContainer = Color(0xFF063B1F),
    secondary = GoldenAmber40,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFDE8C5),
    onSecondaryContainer = Color(0xFF432C00),
    tertiary = SageGreen40,
    background = SoftBeige,
    surface = SoftSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFEAE5DA),
    onSurfaceVariant = TextSecondary,
    outline = WarmBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our handcrafted warm theme for branding consistency
    content: @Composable () -> Unit,
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
        typography = Typography,
        content = content
    )
}
