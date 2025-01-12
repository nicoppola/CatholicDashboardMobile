package ui.main

import ui.theme.LiturgicalColor

data class MainUiState(
    val isToday: Boolean = true,
    val date: String = "",
    val title: String = "",
    val color: LiturgicalColor = LiturgicalColor.GREEN,
    val feasts: List<FeastUiState> = emptyList(),
    val upcoming: List<FeastUiState> = emptyList(),
    val readings: ListItemUiState? = null,
    val office: List<ListItemUiState> = emptyList(),
    val officeOfReadings: ListItemUiState? = null,
    val isRefreshing: Boolean = false,
)

data class FeastUiState(
    val title: String = "",
    val feast: String = "",
    )

data class ListItemUiState(
    val type: ListItemType,
    val isEnabled: Boolean = true,
    val title: String = "",
    val text: String? = null,
    val link: String = "",
)

enum class ListItemType {
    READINGS,
    OFFICE,
    OFFICE_OF_READINGS,
}
