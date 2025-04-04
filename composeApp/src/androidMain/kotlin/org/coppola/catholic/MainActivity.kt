package org.coppola.catholic

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import datastore.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import navigation.RootComponent
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext
import ui.main.App
import ui.theme.CatholicDashboardTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen() //note MUST come before enableEdgeToEdge

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        val root = retainedComponent {
            RootComponent(it)
        }

        val settingsRepo: SettingsRepository by inject()
        val isDarkMode = runBlocking { settingsRepo.getColorMode().first() }

        setContent {
            CatholicDashboardTheme(colorMode = isDarkMode) {
                KoinContext {
                        App(root) { color -> setStatusBarColor(color) }
                }
            }
        }
    }
}

//todo call this inside the main screen or do by themes
@SuppressLint("ComposableNaming")
@Composable
fun setStatusBarColor(color: androidx.compose.ui.graphics.Color) {
    val view = LocalView.current
    SideEffect {
        val barStyle =
            SystemBarStyle.light(color.toArgb(), color.toArgb())
        (view.context as ComponentActivity).enableEdgeToEdge(barStyle, barStyle)
    }
}
