package org.coppola.catholic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import navigation.NavigationStack
import org.koin.compose.KoinContext
import ui.theme.CatholicDashboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CatholicDashboardTheme {
                KoinContext {
                    NavigationStack()
                }
            }
        }
    }
}