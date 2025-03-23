package ui.main

import com.coppola.catholic.Res
import com.coppola.catholic.baseline_calendar_today_24
import org.jetbrains.compose.resources.DrawableResource
import ui.theme.LiturgicalColor

data class MainUiState(
    val isToday: Boolean = true,
    val todayIcon: DrawableResource = Res.drawable.baseline_calendar_today_24,
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
    val header: ListItemHeaderUiState,
    val type: ListItemType,
    val isEnabled: Boolean = true,
    val text: String? = null,
    val link: String = "",
)

data class ListItemHeaderUiState(
    val title: String,
    val isExpanded: Boolean? = null,
    val ctaText: String? = null,
)

enum class ListItemType {
    READINGS,
    OFFICE,
    OFFICE_OF_READINGS,
    FEEDBACK,
}
