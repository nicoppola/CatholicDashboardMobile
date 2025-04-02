package ui.main

import com.coppola.catholic.Res
import com.coppola.catholic.baseline_calendar_today_24
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import ui.theme.LiturgicalColor

data class MainUiState(
    val isToday: Boolean = true,
    val todayIcon: DrawableResource = Res.drawable.baseline_calendar_today_24,
    val date: String = "",
    val currLocalDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val title: String = "",
    val color: LiturgicalColor = LiturgicalColor.GREEN,
    val memorials: FeastsUiState? = null,
    val optionalMemorials: FeastsUiState? = null,
    val upcoming: FeastsUiState? = null,
    val readings: ListItemUiState? = null,
    val liturgyOfHours: List<ListItemUiState> = emptyList(),
    val officeOfReadings: ListItemUiState? = null,
    val isRefreshing: Boolean = false,
)

data class FeastsUiState(
    val title: String = "",
    val feasts: List<String> = emptyList(),
)

data class ListItemUiState(
    val header: ListItemHeaderUiState,
    val type: ListItemType,
    val isEnabled: Boolean = true,
    val text: String? = null,
    val link: String? = null,
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
