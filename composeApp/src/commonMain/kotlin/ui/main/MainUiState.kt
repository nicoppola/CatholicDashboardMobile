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
    val readings: ListCollectionUiState? = null,
    val liturgyOfHours: ListCollectionUiState? = null,
    val officeOfReadings: ListCollectionUiState? = null,
    val isRefreshing: Boolean = false,
)

data class FeastsUiState(
    val title: String = "",
    val feasts: List<String> = emptyList(),
)

data class ListCollectionUiState(
    val header: String,
    val isExpanded: Boolean? = null,
    val items: List<ListCollectionItemUiState>,
)

data class ListCollectionItemUiState(
    val subHeader: String? = null,
    val rows: List<TextRow>,
    val link: String? = null,
    val showOnCollapsed: Boolean = true,
)

data class TextRow(
    val title: String?,
    val text: String?,
)