package xyz.izadi.adar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Accent1_200,
    onPrimary = Accent1_800,
    secondary = Accent2_200,
    onSecondary = Accent2_800,
    background = Neutral1_900,
    onBackground = Neutral1_100,
    surface = Accent2_800,
    onSurface = Neutral1_100
)

private val LightColorPalette = lightColors(
    primary = PrimaryGreen,
    onPrimary = DarkGreen,
    secondary = Accent2_600,
    onSecondary = Accent2_0,
    background = BackgroundGreen,
    onBackground = DarkGreen,
    surface = PrimaryVariantGreen,
    onSurface = DarkGreen
)

@Composable
fun AdarTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}