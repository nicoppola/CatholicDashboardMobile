package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ui.main.App
import ui.settings.SettingsScreen

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(route = Screen.Main.route) {
            App(navToSettings = {
                println("nav to settings " + navController.currentBackStack.value.size)
                navController.navigate(route = Screen.Settings.route) })
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(onBack = {
                println("nav back " + navController.currentBackStack.value.size)
                val ret = navController.popBackStack()
                println(ret)
            })
        }
    }
}