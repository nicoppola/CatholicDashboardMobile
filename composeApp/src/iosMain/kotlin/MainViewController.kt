import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.initKoin
import navigation.RootComponent
import ui.main.App
import ui.theme.CatholicDashboardTheme

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    val root = remember {
        RootComponent(DefaultComponentContext(LifecycleRegistry()))
    }
    CatholicDashboardTheme {
        App(root) {}
    }
}