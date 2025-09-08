package ui.main

import com.coppola.catholic.Res
import com.coppola.catholic.baseline_calendar_today_24
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import ui.theme.LiturgicalColor
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class MainUiState @OptIn(ExperimentalTime::class) constructor(
    val isToday: Boolean = true,
    val todayIcon: DrawableResource = Res.drawable.baseline_calendar_today_24,
    val date: String = "",
    val currLocalDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val title: String = "",
    val color: LiturgicalColor = LiturgicalColor.GREEN,
    val optionalMemorials: List<FeastsUiState> = emptyList(),
    val upcoming: FeastsUiState? = null,
    val readings: SectionUiState? = null,
    val liturgyOfHours: SectionUiState? = null,
    val rosary: SectionUiState? = null,
    val isLoading: Boolean = false,
    val canSelectNext: Boolean = true,
    val canSelectPrevious: Boolean = true,
)

data class FeastsUiState(
    val title: String = "",
    val feasts: List<String> = emptyList(),
)

data class SectionUiState(
    val header: String,
    val isExpanded: Boolean? = null,
    val items: List<SectionItem>,
)

data class SectionItem(
    val subHeader: String? = null,
    val rows: List<TextRow>,
    val link: String? = null,
    val showOnCollapsed: Boolean = true,
)

data class TextRow(
    val title: String?,
    val text: String?,
)