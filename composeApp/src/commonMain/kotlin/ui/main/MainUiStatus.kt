package ui.main

sealed class MainUiStatus{
    data object NavToSettings: MainUiStatus()
    data object LoadingComplete: MainUiStatus()
}
