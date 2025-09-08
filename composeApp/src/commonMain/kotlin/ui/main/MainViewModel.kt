package ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.DayData
import data.V2MainRepository
import domain.GetTodayIconUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.DateTimeUnit.Companion.MONTH
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import util.contains
import util.onError
import util.onSuccess
import util.toMeridianTime
import kotlin.time.Clock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@OptIn(ExperimentalTime::class)
class MainViewModel(
    private val newRepo: V2MainRepository,
    private val getTodayIconUseCase: GetTodayIconUseCase,
) : ViewModel() {

    private var currDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private var today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    private var currData: DayData? = null

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _uiStatus = MutableStateFlow<MainNavStatus?>(null)
    val navStatus: StateFlow<MainNavStatus?> = _uiStatus.asStateFlow()

    //////////////////////////////// INIT ////////////////////////////////

    init {
        retrieveData()
    }

    fun retrieveData() {
        viewModelScope.launch {
            _uiState.update {
                _uiState.value.copy(isLoading = true)
            }
            setNextPreviousButtons()
            newRepo.retrieveData(currDate)
                .onSuccess { data ->
                    currData = data
                    val currTime =
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
                    _uiState.update { curr ->
                        curr.copy(
                            date = data.date,
                            currLocalDate = currDate,
                            isToday = currDate == today,
                            todayIcon = getTodayIconUseCase(today),
                            title = data.title,
                            optionalMemorials = data.secondaryCelebrations.map {
                                FeastsUiState(title = it.rankTitle, feasts = it.titles)
                            },
                            readings =
                                ListCollectionUiState(
                                    header = data.readingSections.sectionTitle,
                                    isExpanded = if (data.readingSections.subSections.size > 1) false else null,
                                    items = data.readingSections.subSections.mapIndexed { i, it ->
                                        ListCollectionItemUiState(
                                            subHeader = if (data.readingSections.subSections.size > 1) it.sectionSubtitle else null,
                                            rows = it.readingTitles.map { j ->
                                                TextRow(j.title, j.verses)
                                            },
                                            link = it.link,
                                            showOnCollapsed = i == 0
                                        )
                                    }
                                ),
                            liturgyOfHours = ListCollectionUiState(
                                header = data.liturgyHours.sectionTitle,
                                isExpanded = false,
                                items = data.liturgyHours.hours.map {
                                    ListCollectionItemUiState(
                                        subHeader = null,
                                        rows = listOf(
                                            TextRow(
                                                it.id.novusLabel,
                                                "${it.id.timeStart.toMeridianTime()} - ${it.id.timeEnd.toMeridianTime()}"
                                            )
                                        ),
                                        link = it.link,
                                        showOnCollapsed = it.id.contains(currTime)
                                    )
                                }
                            ),
                            rosary = ListCollectionUiState(
                                header = data.rosary.sectionTitle,
                                isExpanded = null,
                                items = listOf(
                                    ListCollectionItemUiState(
                                        subHeader = data.rosary.rosary.mysteryTitle,
                                        link = data.rosary.rosary.link,
                                        rows = data.rosary.rosary.mysteries.map {
                                            TextRow(
                                                title = null,
                                                text = it,
                                            )
                                        }
                                    )
                                )
                            )
                        )
                    }
                    _uiState.update {
                        _uiState.value.copy(isLoading = false)
                    }
                }.onError { data ->
                    println("***** ERROR $data")
                    println("ERROR!!!!!!!")
                    _uiState.update { _ ->
                        uiState.value.copy(
                            date = "ERROR!!! AHHHHHHHH",
                            title = data.name,
                        )
                    }
                    _uiState.update {
                        _uiState.value.copy(isLoading = false)
                    }
                }
        }
    }

    private fun setNextPreviousButtons() {
        _uiState.update {
            _uiState.value.copy(canSelectNext = isDateSelectable(currDate.plus(1, DAY)))
        }
        _uiState.update {
            _uiState.value.copy(canSelectPrevious = isDateSelectable(currDate.minus(1, DAY)))
        }
    }

//////////////////////////////// Click Handlers ////////////////////////////////

    fun onSettingsClicked() {
        _uiStatus.update {
            MainNavStatus.NavToSettings
        }
    }

    fun clearUiStatus() {
        _uiStatus.update {
            null
        }
    }

    fun onTodayClicked() {
        currDate = today
        retrieveData()
    }

    fun onLitHoursExpandButton(isExpanded: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(liturgyOfHours = it.liturgyOfHours?.copy(isExpanded = isExpanded))
            }
        }
    }

    fun onReadingsExpandButton(isExpanded: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(readings = it.readings?.copy(isExpanded = isExpanded))
            }
        }
    }

    fun onPreviousDateButton() {
        currDate = currDate.minus(DatePeriod(days = 1))
        retrieveData()
    }

    fun onNextDateButton() {
        currDate = currDate.plus(DatePeriod(days = 1))
        retrieveData()
    }

    fun onDateSelected(newDateMillis: Long) {
        currDate = millisToLocalDate(newDateMillis)
        retrieveData()
    }

    fun isYearSelectable(year: Int): Boolean {
        val furthestYear = today.plus(6, MONTH).year
        val earliestYear = today.minus(6, MONTH).year

        return year >= earliestYear && year <= furthestYear
    }

    fun isDateSelectable(dateMillis: Long): Boolean {
        val date = millisToLocalDate(dateMillis)
        return isDateSelectable(date)
    }

    // if is within a year of current date
    private fun isDateSelectable(date: LocalDate): Boolean {
        val furthestDate = today.plus(6, MONTH)
        val earliestDate = today.minus(6, MONTH)

        return date >= earliestDate && date <= furthestDate
    }

    private fun updateLiturgyOfTheHours() {
        currData?.let { data ->
            val currTime =
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
            _uiState.update {
                it.copy(
                    liturgyOfHours = ListCollectionUiState(
                        header = data.liturgyHours.sectionTitle,
                        isExpanded = false,
                        items = data.liturgyHours.hours.map { hour ->
                            ListCollectionItemUiState(
                                subHeader = null,
                                rows = listOf(
                                    TextRow(
                                        hour.id.novusLabel,
                                        "${hour.id.timeStart.toMeridianTime()} - ${hour.id.timeEnd.toMeridianTime()}"
                                    )
                                ),
                                link = hour.link,
                                showOnCollapsed = hour.id.contains(currTime)
                            )
                        }
                    ),
                )
            }
        }
    }

    private fun millisToLocalDate(millis: Long): LocalDate {
        val days = millis.toDuration(DurationUnit.MILLISECONDS)
            .inWholeDays
            .toInt()

        return LocalDate.fromEpochDays(days)
    }

    fun onResume() {
        println("************** ON RESUME ")
        val newToday = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (today != newToday) {
            today = newToday
            currDate = today
            retrieveData()
        } else {
            updateLiturgyOfTheHours()
        }
    }
}
