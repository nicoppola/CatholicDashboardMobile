import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin
import ui.main.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}