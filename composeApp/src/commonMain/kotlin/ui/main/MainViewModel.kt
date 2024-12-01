package ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun retrieveData() {
        viewModelScope.launch {
            repo.retrieveData()
                .onSuccess { data ->
                    val startData = _uiState.value
                    println("***** SUCCESS $data")
                    _uiState.update { curr ->
                        curr.copy(
                            date = data.date,
                            title = data.title,
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
                        getOfficeOfReadingsListItemUseCase().collect { newItem ->
                            _uiState.update {
                                it.copy(officeOfReadings = newItem)
                            }
                        }
                    }
                    viewModelScope.launch {
                        getReadingsListItemUseCase().collect { newItem ->
                            _uiState.update {
                                it.copy(readings = newItem)
                            }
                        }
                    }
                    viewModelScope.launch {
                        getOfficeListItemUseCase().collect { newItem ->
                            _uiState.update {
                                uiState.value.copy(office = newItem)
                            }
                        }
                    }

                    if (startData == _uiState.value) {
                        delay(2000)
                    }
                    _uiStatus.update {
                        MainUiStatus.LoadingComplete
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
                    _uiStatus.update {
                        MainUiStatus.LoadingComplete
                    }
                }
        }
    }
}
