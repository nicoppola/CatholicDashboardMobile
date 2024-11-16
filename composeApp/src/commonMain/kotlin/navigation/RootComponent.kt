package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<NavConfig>()

    val navDestinationStack: Value<ChildStack<NavConfig, NavDestination>> = childStack(
        source = navigation,
        serializer = NavConfig.serializer(),
        initialConfiguration = NavConfig.Main,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: NavConfig,
        context: ComponentContext,
    ): NavDestination {
        return when (config) {
            is NavConfig.Main -> {
                NavDestination.Main(
                    MainComponent(
                        context,
                        onNavigateToSettings = {
                            navigation.pushNew(NavConfig.Settings)
                        },
                        onNavigateToWebView = {url ->
                            navigation.pushNew(NavConfig.WebView(url))
                        }
                    )
                )
            }
            is NavConfig.Settings -> {
                NavDestination.Settings(
                    SettingsComponent(
                        context,
                        onNavBack = {
                            navigation.pop()
                        }
                    )
                )
            }
            is NavConfig.WebView -> {
                NavDestination.WebView(
                    WebViewComponent(
                        config.url,
                        context,
                        onNavBack = {
                            navigation.pop()
                        }
                    )
                )
            }
        }
    }

}