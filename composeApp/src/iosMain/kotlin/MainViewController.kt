import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin
import navigation.NavigationStack
import ui.main.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    NavigationStack()
}