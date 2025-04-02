package ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import navigation.NavDestination
import navigation.RootComponent
import ui.settings.SettingsScreen
import ui.webview.WebViewScreen

@Composable
fun App(root: RootComponent, setStatusBarColor: @Composable (Color) -> Unit) {
    val childStack by root.navDestinationStack.subscribeAsState()
    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        when (val instance = child.instance) {
            is NavDestination.Main -> MainScreen(
                instance.component,
                setStatusBarColor = { color -> setStatusBarColor(color) },
            )
            is NavDestination.Settings -> SettingsScreen(instance.component)
            is NavDestination.WebView -> WebViewScreen(instance.component)
        }
    }
}