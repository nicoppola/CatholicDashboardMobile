package ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import catholicdashboard.composeapp.generated.resources.Res
import catholicdashboard.composeapp.generated.resources.breviary
import catholicdashboard.composeapp.generated.resources.readings
import data.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlinx.datetime.todayIn
import networking.NovusResponse
import ui.theme.LiturgicalColor
import util.onError
import util.onSuccess

class MainViewModel(
    private val repo: MainRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val dateFormat = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
        chars(", ")
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        dayOfMonth()
        chars(", ")
        year()
    }

    init {
        update()
    }

    fun update(){
        viewModelScope.launch {
            _uiState.update {
                uiState.value.copy(isLoading = true)
            }
            setCalendarInfo()
            _uiState.update {
                uiState.value.copy(
                    listObjects = listOf(
                        getReadingsItem(),
                        getOficeItem()
                    ),
                    upcoming = getUpcoming(),
                )
            }
            _uiState.update {
                uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun getUpcoming(): List<FeastUiState>{
        return listOf(
            FeastUiState(
                title = "Upcoming Holy Day of Obligation",
                feast = "- Thursday: Assumption of the Blessed Virgin Mary, Solemnity"
            ),
            FeastUiState(
                title = "Upcoming",
                feast = "- Saint Maximilian Mary Kolbe, priest and martyr, memorial"
            ),
            FeastUiState(
                title = "Upcoming",
                feast = "- St. Michael's Lent"
            )
        )
    }

    private fun getReadingsItem(): ListObject{
        return ListObject(
            icon = Res.drawable.readings,
            headline = "Daily Readings",
            subText = "Matt 18:1-5, 10, 12-14  \n\"Unless you turn and become like children, you will not enter the Kingdom of heaven\"",
            link = getReadingsLink()
        )
    }

    private fun getOficeItem(): ListObject{
        return ListObject(
            icon = Res.drawable.breviary,
            headline = "Divine Office",
            subText = "Evening Prayer 4:00p - 6:00p",
            link = getOfficeLink()
        )
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    private fun getOfficeLink(): String {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            .format(
                LocalDate.Format {
                    byUnicodePattern("yyyyMMdd")
                })

        return "https://divineoffice.org/?date=$today"
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    private fun getReadingsLink(): String {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            .format(
                LocalDate.Format {
                    byUnicodePattern("MMddyy")
                })
        return "https://bible.usccb.org/bible/readings/$today.cfm"
    }

    private suspend fun setCalendarInfo() {
        repo.retrieveData()
            .onSuccess { data ->
                _uiState.update { _ ->
                    uiState.value.copy(
                        date = formatDate(data),
                        season = getSeason(data) ?: "",
                        feasts = getFeasts(data),
                        color = LiturgicalColor.GREEN,
                    )
                }
            }
            .onError { data ->
                _uiState.update { _ ->
                    uiState.value.copy(
                        date = data.name
                    )
                }

            }
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    private fun formatDate(data: NovusResponse): String {
        return LocalDate
            .parse(
                data.date ?: "",
                LocalDate.Format { byUnicodePattern("yyyy-MM-dd") }
            )
            .format(dateFormat)
    }

    private fun getSeason(data: NovusResponse): String? {
        val celebration =
            data.celebrations?.find {
                data.season?.let { it1 ->
                    it.title?.contains(
                        it1,
                        ignoreCase = true
                    )
                } == true
            }
                ?: data.celebrations?.first()
        // returns "Saturday, 12th week in Ordinary Time"; I'm removing "Saturday,"
        return celebration?.title?.substring(startIndex = celebration.title.indexOfFirst { it == ',' } + 1)
    }

    // lower rank number == higher priority
    private fun getFeasts(data: NovusResponse): List<FeastUiState> {
        val byRank = data.celebrations
            ?.sortedBy { it.rank_num }
            ?.filter {
                !data.season?.let { it1 ->
                    it.title?.contains(
                        it1,
                        ignoreCase = true
                    )
                }!!
            } // remove the feria/etc
//            .map{ FeastUiState.Feast(title = it.title, color = it.colour) }
            ?.groupBy { it.rank }

        val feastsUiStates = mutableListOf<FeastUiState>()
        byRank?.forEach {
            it.value.forEach { celebration ->
                feastsUiStates.add(FeastUiState(title = it.key ?: "", feast = celebration.title ?: ""))
            }
        }
    return feastsUiStates
}

}