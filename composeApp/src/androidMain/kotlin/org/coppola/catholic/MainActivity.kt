package org.coppola.catholic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import navigation.RootComponent
import org.koin.compose.KoinContext
import ui.main.App
import ui.theme.CatholicDashboardTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = retainedComponent {
            RootComponent(it)
        }
        installSplashScreen()

        setContent {
            CatholicDashboardTheme {
                KoinContext {
                    App(root)
                }
            }
        }
    }
}