package com.serj.matchuphelper.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Gold = Color(0xFFC8AA6E)
private val GoldDark = Color(0xFFA08A54)
private val GoldLight = Color(0xFFE0CDA0)
private val Teal = Color(0xFF0AC8B9)
private val TealDark = Color(0xFF08A89B)

private val DarkGround = Color(0xFF0D0F12)
private val DarkSurface = Color(0xFF14181E)
private val DarkSurfaceVariant = Color(0xFF1C2028)

private val LightGround = Color(0xFFF5F1EA)
private val LightSurface = Color(0xFFFFFFFF)
private val LightSurfaceVariant = Color(0xFFEDEAE3)

private val WinGreen = Color(0xFF49B04A)
private val LossRed = Color(0xFFE84057)

val DarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = Color(0xFF1A1714),
    primaryContainer = Color(0xFF2A2318),
    onPrimaryContainer = GoldLight,
    secondary = Teal,
    onSecondary = Color(0xFF0D0F12),
    secondaryContainer = Color(0xFF0E2A28),
    onSecondaryContainer = Color(0xFF9AE8E0),
    tertiary = WinGreen,
    onTertiary = Color.White,
    error = LossRed,
    onError = Color.White,
    background = DarkGround,
    onBackground = Color(0xFFD8D4CE),
    surface = DarkSurface,
    onSurface = Color(0xFFD8D4CE),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFF8A8478),
    outline = Color(0xFF3A3630),
    outlineVariant = Color(0xFF282D36),
)

val LightColorScheme = lightColorScheme(
    primary = GoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF5EDD8),
    onPrimaryContainer = Color(0xFF3A3018),
    secondary = TealDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD0F5F0),
    onSecondaryContainer = Color(0xFF0A3A36),
    tertiary = WinGreen,
    onTertiary = Color.White,
    error = LossRed,
    onError = Color.White,
    background = LightGround,
    onBackground = Color(0xFF1A1714),
    surface = LightSurface,
    onSurface = Color(0xFF1A1714),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF6B6358),
    outline = Color(0xFFD4CFC4),
    outlineVariant = Color(0xFFE3DFD6),
)

@Composable
fun MatchupHelperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
