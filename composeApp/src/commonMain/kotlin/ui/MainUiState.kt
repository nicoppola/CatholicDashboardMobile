package ui

import org.jetbrains.compose.resources.DrawableResource
import ui.theme.LiturgicalColor

data class MainUiState(
    val date: String = "",
    val season: String = "",
    val color: LiturgicalColor = LiturgicalColor.GREEN,
    val feasts: List<FeastUiState> = emptyList(),
    val upcoming: List<FeastUiState> = emptyList(),
    val listObjects: List<ListObject> = emptyList(),
    val isLoading: Boolean = false,
)

data class FeastUiState(
    val title: String = "",
    val feast: String = "",
    )

data class ListObject(
    val icon: DrawableResource? = null,
    val headline: String = "",
    val subText: String? = null,
    val link: String = "",
)
