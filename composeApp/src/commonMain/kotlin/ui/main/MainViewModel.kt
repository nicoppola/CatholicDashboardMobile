package ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.CalendarData
import data.MainRepository
import domain.GetLiturgyOfHoursListItemUseCase
import domain.GetOfficeOfReadingsListItemUseCase
import domain.GetReadingsListItemUseCase
import domain.GetTodayIconUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import ui.theme.LiturgicalColor
import util.onError
import util.onSuccess

class MainViewModel(
    private val repo: MainRepository,
    private val getLiturgyOfHoursListItemUseCase: GetLiturgyOfHoursListItemUseCase,
    private val getReadingsListItemUseCase: GetReadingsListItemUseCase,
    private val getOfficeOfReadingsListItemUseCase: GetOfficeOfReadingsListItemUseCase,
    private val getTodayIconUseCase: GetTodayIconUseCase,
) : ViewModel() {

    private var currDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private var today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

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
                _uiState.value.copy(isRefreshing = true)
            }
            repo.retrieveData(currDate)
                .onSuccess { data ->
                    val startData = _uiState.value
                    println("***** SUCCESS $data")
                    if (data.propers.find { it.rank != CalendarData.Rank.MEMORIAL && it.rank != CalendarData.Rank.OPTIONAL_MEMORIAL } != null) {
                        println("************** UNKNOWN PROPER **************")
                    }
                    _uiState.update { curr ->
                        curr.copy(
                            date = data.date ?: "",
                            currLocalDate = currDate,
                            isToday = currDate == today,
                            todayIcon = getTodayIconUseCase(today),
                            title = data.title ?: "",
                            color = data.color?.name?.let { LiturgicalColor.fromName(it) }
                                ?: LiturgicalColor.GREEN,
                            memorials = data.propers.filter { it.rank == CalendarData.Rank.MEMORIAL }
                                .let { memorials ->
                                    if (memorials.isNotEmpty()) {
                                        FeastsUiState(
                                            title = "Memorials",
                                            feasts = memorials.mapNotNull { it.title }
                                        )
                                    } else {
                                        null
                                    }

                                },
                            optionalMemorials = data.propers.filter { it.rank == CalendarData.Rank.OPTIONAL_MEMORIAL }
                                .let { optionalMemorials ->
                                    if (optionalMemorials.isNotEmpty()) {
                                        FeastsUiState(
                                            title = "Optional Memorials",
                                            feasts = optionalMemorials.mapNotNull { it.title }
                                        )
                                    } else {
                                        null
                                    }

                                },
                        )
                    }

                    //update office of readings
                    getOfficeOfReadingsListItemUseCase(currDate).let { newItem ->
                        _uiState.update {
                            it.copy(officeOfReadings = newItem)
                        }
                    }

                    //update readings
                    getReadingsListItemUseCase(currDate).let { newItem ->
                        _uiState.update {
                            it.copy(readings = newItem)
                        }
                    }

                    updateLiturgyOfHours(isExpanded = uiState.value.liturgyOfHours?.isExpanded == true)

                    if (startData == _uiState.value) {
                        delay(2000)
                    }
                    _uiState.update {
                        _uiState.value.copy(isRefreshing = false)
                    }
                }
                .onError { data ->
                    println("***** ERROR $data")
                    println("ERROR!!!!!!!")
                    _uiState.update { _ ->
                        uiState.value.copy(
                            date = "ERROR!!! AHHHHHHHH",
                            title = data.name,
                        )
                    }
                    _uiState.update {
                        _uiState.value.copy(isRefreshing = false)
                    }
                }
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

    fun onDateSelected(newDate: LocalDate?) {
        if (newDate != null) {
            currDate = newDate
            retrieveData()
        }
    }

    fun onResume() {
        println("************** ON RESUME ")
        val newToday = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (today != newToday) {
            today = newToday
            currDate = today
            retrieveData()
        }
    }

    //////////////////////////////// Private Funs ////////////////////////////////

    private suspend fun updateLiturgyOfHours(isExpanded: Boolean) {
        getLiturgyOfHoursListItemUseCase(currDate, isExpanded).let { newItem ->
            _uiState.update {
                uiState.value.copy(liturgyOfHours = newItem)
            }
        }
    }
}
