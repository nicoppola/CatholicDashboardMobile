package ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coppola.catholic.Res
import com.coppola.catholic.baseline_calendar_today_24
import com.coppola.catholic.calendar_1
import com.coppola.catholic.calendar_2
import com.coppola.catholic.calendar_3
import com.coppola.catholic.calendar_4
import com.coppola.catholic.calendar_5
import com.coppola.catholic.calendar_6
import com.coppola.catholic.calendar_7
import com.coppola.catholic.calendar_8
import com.coppola.catholic.calendar_9
import com.coppola.catholic.calendar_10
import com.coppola.catholic.calendar_11
import com.coppola.catholic.calendar_12
import com.coppola.catholic.calendar_13
import com.coppola.catholic.calendar_14
import com.coppola.catholic.calendar_15
import com.coppola.catholic.calendar_16
import com.coppola.catholic.calendar_17
import com.coppola.catholic.calendar_18
import com.coppola.catholic.calendar_19
import com.coppola.catholic.calendar_20
import com.coppola.catholic.calendar_21
import com.coppola.catholic.calendar_22
import com.coppola.catholic.calendar_23
import com.coppola.catholic.calendar_24
import com.coppola.catholic.calendar_25
import com.coppola.catholic.calendar_26
import com.coppola.catholic.calendar_27
import com.coppola.catholic.calendar_28
import com.coppola.catholic.calendar_29
import com.coppola.catholic.calendar_30
import com.coppola.catholic.calendar_31
import data.CalendarData
import data.MainRepository
import datastore.PreferencesRepository
import domain.GetOfficeListItemUseCase
import domain.GetOfficeOfReadingsListItemUseCase
import domain.GetReadingsListItemUseCase
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
import org.jetbrains.compose.resources.DrawableResource
import ui.theme.LiturgicalColor
import util.addNotNull
import util.onError
import util.onSuccess

class MainViewModel(
    private val repo: MainRepository,
    private val getOfficeListItemUseCase: GetOfficeListItemUseCase,
    private val getReadingsListItemUseCase: GetReadingsListItemUseCase,
    private val getOfficeOfReadingsListItemUseCase: GetOfficeOfReadingsListItemUseCase,
) : ViewModel() {

    private var currDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _uiStatus = MutableStateFlow<MainUiStatus?>(null)
    val uiStatus: StateFlow<MainUiStatus?> = _uiStatus.asStateFlow()


    init {
        retrieveData()
    }

    fun onSettingsClicked() {
        _uiStatus.update {
            MainUiStatus.NavToSettings
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

    fun onLitHoursButton(isExpanded: Boolean) {
        updateLiturgyOfHours(isExpanded)
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

    fun retrieveData() {
        viewModelScope.launch {
            _uiState.update {
                _uiState.value.copy(isRefreshing = true)
            }
            repo.retrieveData(currDate)
                .onSuccess { data ->
                    val startData = _uiState.value
                    println("***** SUCCESS $data")
                    _uiState.update { curr ->
                        curr.copy(
                            date = data.date,
                            currLocalDate = currDate,
                            isToday = currDate == today,
                            todayIcon = getTodayIcon(),
                            title = data.readings.title ?: "", // RIP my calendar data.title,
                            color = LiturgicalColor.fromName(data.color.name)
                                ?: LiturgicalColor.GREEN,
                            feasts = data.proper.mapNotNull {
                                if (it.title != null && !(data.title.contains(it.title))) {
                                    FeastUiState(it.title)
                                } else {
                                    null
                                }
                            },
                        )
                    }
                    viewModelScope.launch {
                        getOfficeOfReadingsListItemUseCase(currDate).collect { newItem ->
                            _uiState.update {
                                it.copy(officeOfReadings = newItem)
                            }
                        }
                    }
                    viewModelScope.launch {
                        getReadingsListItemUseCase(currDate).collect { newItem ->
                            _uiState.update {
                                it.copy(readings = newItem)
                            }
                        }
                    }

                    updateLiturgyOfHours(isExpanded = uiState.value.officeOfReadings?.header?.isExpanded == true)

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

    private fun updateLiturgyOfHours(isExpanded: Boolean) {
        viewModelScope.launch {
            getOfficeListItemUseCase(currDate, isExpanded).collect { newItem ->
                _uiState.update {
                    uiState.value.copy(office = newItem)
                }
            }
        }
    }

    private fun getTodayIcon(): DrawableResource {
        return when (today.dayOfMonth) {
            1 -> Res.drawable.calendar_1
            2 -> Res.drawable.calendar_2
            3 -> Res.drawable.calendar_3
            4 -> Res.drawable.calendar_4
            5 -> Res.drawable.calendar_5
            6 -> Res.drawable.calendar_6
            7 -> Res.drawable.calendar_7
            8 -> Res.drawable.calendar_8
            9 -> Res.drawable.calendar_9
            10 -> Res.drawable.calendar_10
            11 -> Res.drawable.calendar_11
            12 -> Res.drawable.calendar_12
            13 -> Res.drawable.calendar_13
            14 -> Res.drawable.calendar_14
            15 -> Res.drawable.calendar_15
            16 -> Res.drawable.calendar_16
            17 -> Res.drawable.calendar_17
            18 -> Res.drawable.calendar_18
            19 -> Res.drawable.calendar_19
            20 -> Res.drawable.calendar_20
            21 -> Res.drawable.calendar_21
            22 -> Res.drawable.calendar_22
            23 -> Res.drawable.calendar_23
            24 -> Res.drawable.calendar_24
            25 -> Res.drawable.calendar_25
            26 -> Res.drawable.calendar_26
            27 -> Res.drawable.calendar_27
            28 -> Res.drawable.calendar_28
            29 -> Res.drawable.calendar_29
            30 -> Res.drawable.calendar_30
            31 -> Res.drawable.calendar_31
            else -> Res.drawable.baseline_calendar_today_24
        }
    }
}
