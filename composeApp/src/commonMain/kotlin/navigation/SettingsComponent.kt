package navigation

import com.arkivanov.decompose.ComponentContext

class SettingsComponent(
    componentContext: ComponentContext,
    private val onNavBack: () -> Unit,
): ComponentContext by componentContext {

    fun onBack() {
        onNavBack()
    }
}