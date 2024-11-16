package navigation

import com.arkivanov.decompose.ComponentContext

class MainComponent(
    componentContext: ComponentContext,
    private val onNavigateToSettings: () -> Unit,
    private val onNavigateToWebView: (String) -> Unit,
): ComponentContext by componentContext {

    fun onNavSettings(){
        onNavigateToSettings()
    }

    fun onNavWebView(url: String){
        onNavigateToWebView(url)
    }
}