package ui.main

import ui.theme.LiturgicalColor

data class MainUiState(
    val date: String = "",
    val title: String = "",
    val color: LiturgicalColor = LiturgicalColor.GREEN,
    val feasts: List<FeastUiState> = emptyList(),
    val upcoming: List<FeastUiState> = emptyList(),
    val listObjects: List<ListItemUiState> = emptyList(),
    val isLoading: Boolean = false,
)

data class FeastUiState(
    val title: String = "",
    val feast: String = "",
    )

data class ListItemUiState(
    val title: String = "",
    val text: String? = null,
    val link: String = "",
)
