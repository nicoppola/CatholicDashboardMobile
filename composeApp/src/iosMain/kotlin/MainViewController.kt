import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import datastore.SettingsRepository
import di.initKoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import navigation.RootComponent
import org.koin.compose.koinInject
import platform.UIKit.UIViewController
import ui.main.App
import ui.theme.CatholicDashboardTheme


fun MainViewController() : UIViewController = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    val root = remember {
        RootComponent(DefaultComponentContext(LifecycleRegistry()))
    }

    val settingsRepo: SettingsRepository = koinInject<SettingsRepository>()
    val colorMode = runBlocking { settingsRepo.getColorMode().first() }

    CatholicDashboardTheme(colorMode) {
        App(root) {}
    }
}