package vn.finnest.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Clay = Color(0xFF8C302B)
private val Copper = Color(0xFFDDA65D)
private val Leaf = Color(0xFF617A55)
private val Cream = Color(0xFFF8F1E7)
private val Ink = Color(0xFF282522)
private val DarkBackground = Color(0xFF171513)

private val LightColors = lightColorScheme(
    primary = Clay,
    secondary = Copper,
    tertiary = Leaf,
    background = Cream,
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Ink,
    onSurface = Ink
)
private val DarkColors = darkColorScheme(
    primary = Color(0xFFE8A19A),
    secondary = Copper,
    tertiary = Color(0xFFB5CBA7),
    background = DarkBackground,
    surface = Color(0xFF24211E)
)

@Composable
fun FinNestTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, typography = androidx.compose.material3.Typography(), content = content)
}
