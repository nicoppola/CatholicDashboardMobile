package navigation

import com.arkivanov.decompose.ComponentContext

class WebViewComponent(
    val url: String,
    val title: String,
    componentContext: ComponentContext,
    private val onNavBack: () -> Unit,
) : ComponentContext by componentContext {

    fun onBack() {
        onNavBack()
    }
}