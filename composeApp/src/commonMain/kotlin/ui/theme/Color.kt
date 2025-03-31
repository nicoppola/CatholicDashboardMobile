package ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val primaryWhite = Color.White
val secondaryWhite = Color(0xFFEFEFEF)
val tertiaryWhite = Color(0xBFFFFFFF)

val darkGray = Color(0xFF1C1B1D)


val primaryDark = Color(0xFF1D1B20)
val onPrimaryDark = Color(0xFFE6E0E9)
val primaryContainerDark = Color(0xFF36343B)
val onPrimaryContainerDark = Color(0xFFE9E9E9)
val tertiaryDark = Color(0x66BB6A)

val primaryLight = Color(0xFFF2F2F7)
val onPrimaryLight = Color(0xFF0E0E0E)
val primaryContainerLight = Color(0xFFFFFFFF)
val onPrimaryContainerLight = Color(0xFF1B1B1B)
val tertiaryLight = Color(0x66BB6A)



enum class LiturgicalColor(val color: Color, val title: String) {
    GREEN(color = Color(0xFF00662F), title = "green"),
    VIOLET(color = Color(0XFF350066), title = "purple"),
    RED(color = Color(0xff660000), title = "red"),
    ROSE(color = Color(0xffab1897), title = "rose"),
    WHITE(color = Color.White, title = "white");

    companion object{
        fun fromName(inName: String): LiturgicalColor? {
            return entries.find { inName.toLowerCase(Locale.current) == it.title }
        }
    }
}