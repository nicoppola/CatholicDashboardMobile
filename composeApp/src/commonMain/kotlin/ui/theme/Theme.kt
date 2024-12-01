package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration


private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,

    ///* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    //  */
)

private val OrdinaryColorScheme = lightColorScheme(
    primary = Color(0xFF006630),
    secondary = Color(0xFFFFFBFE),

    onPrimary = Color.White,
    onSecondary = Color.Black,
)

private val PennetentialColorScheme = lightColorScheme(
    primary = Color(0xFF350066),
    secondary = Color(0xFFFFFBFE),

    onPrimary = Color.White,
    onSecondary = Color.Black,
)

private val MartyrColorScheme = lightColorScheme(
    primary = Color(0xFF700000),
    secondary = Color(0xFFFFFBFE),

    onPrimary = Color.White,
    onSecondary = Color.Black,
)

private val RoseColorScheme = lightColorScheme(
    primary = Color(0xFF9C168A),
    secondary = Color(0xFFFFFBFE),

    onPrimary = Color.White,
    onSecondary = Color.Black,
)

private val SolemnityColorScheme = lightColorScheme(
    primary = Color(0xFFF0F0F0),
    secondary = Color(0xFFFFFBFE),

    onPrimary = Color.White,
    onSecondary = Color.Black,
)

@Composable
fun CatholicDashboardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme =
            if (darkTheme) DarkColorScheme else LightColorScheme


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}