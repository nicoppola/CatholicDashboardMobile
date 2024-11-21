package org.coppola.catholic

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import navigation.RootComponent
import org.koin.compose.KoinContext
import ui.main.App
import ui.theme.CatholicDashboardTheme
import ui.theme.LiturgicalColor


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

        setContent {
            CatholicDashboardTheme {
                setStatusBarColor(LiturgicalColor.GREEN.color)
                KoinContext {
                    App(root)
                }
            }
        }
    }
}

//todo call this inside the main screen or do by themes
@SuppressLint("ComposableNaming")
@Composable
fun setStatusBarColor(color: androidx.compose.ui.graphics.Color){
    val view = LocalView.current
    SideEffect {
        val barStyle =
             SystemBarStyle.light(color.toArgb(), color.toArgb())
        (view.context as ComponentActivity).enableEdgeToEdge(barStyle, barStyle)
    }
//    if(!view.isInEditMode) {
//        LaunchedEffect(true) {
//            val window = (view.context as Activity).window
//            window.statusBarColor = color.toArgb()
//            window.navigationBarColor
//
//        }
//    }
}
