package ui.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import navigation.NavDestination
import navigation.RootComponent
import org.koin.core.annotation.KoinExperimentalAPI
import ui.settings.SettingsScreen

@Composable
fun App(root: RootComponent) {
    val childStack by root.navDestinationStack.subscribeAsState()
    Children(
        stack = childStack,
        animation = stackAnimation(slide())
    ) { child ->
        when (val instance = child.instance) {
            is NavDestination.Main -> MainScreen(instance.component)
            is NavDestination.Settings -> SettingsScreen(instance.component)
            is NavDestination.WebView -> TODO()
        }
    }
}