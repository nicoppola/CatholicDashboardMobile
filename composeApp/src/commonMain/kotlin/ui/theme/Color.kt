package ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val primaryWhite = Color.White
val secondaryWhite = Color(0xFFEFEFEF)
val tertiaryWhite = Color(0xBFFFFFFF)

enum class LiturgicalColor(val color: Color, val title: String) {
    GREEN(color = Color(0xFF00662F), title = "green"),
    VIOLET(color = Color(0XFF350066), title = "violet"),
    RED(color = Color(0xff660000), title = "red"),
    ROSE(color = Color(0xffab1897), title = "rose"),
    WHITE(color = Color.White, title = "white");

    companion object{
        fun fromName(inName: String): LiturgicalColor? {
            return entries.find { inName == it.title }
        }
    }
}