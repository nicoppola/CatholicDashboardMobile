package navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavConfig(val route: String) {
    @Serializable
    data object Main: NavConfig("main_screen")
    @Serializable
    data object Settings: NavConfig("settings_screen")
    @Serializable
    data class WebView(val url: String) : NavConfig("webview")
}

sealed class NavDestination {
    data class Main(val component: MainComponent): NavDestination()
    data class Settings(val component: SettingsComponent): NavDestination()
    data class WebView(val component: WebViewComponent): NavDestination()
}