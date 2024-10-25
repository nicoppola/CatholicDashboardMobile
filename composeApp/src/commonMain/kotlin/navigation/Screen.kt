package navigation

sealed class Screen(val route: String) {
    data object Main: Screen("main_screen")
    data object Settings: Screen("settings_screen")
}