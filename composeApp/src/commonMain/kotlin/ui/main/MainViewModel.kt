package ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.CalendarData
import data.MainRepository
import datastore.PreferencesRepository
import domain.GetOfficeListItemUseCase
import domain.GetOfficeOfReadingsListItemUseCase
import domain.GetReadingsListItemUseCase
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

    // todo cache this in the backend and get it from there
    private var currData: CalendarData.Day? = null

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
            _uiState.update {
                it.copy(isLoading = true)
            }
            repo.retrieveData()
                .onSuccess { data ->
                    currData = data
                    println("***** SUCCESS $data")
                    _uiState.update { curr ->
                        curr.copy(
                            isLoading = false,
                            date = data.date,
                            title = data.title,
                            color = LiturgicalColor.fromName(data.color.name)
                                ?: LiturgicalColor.GREEN,
                            feasts = data.proper.map { FeastUiState(it.title ?: "") },
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
                }
            _uiState.update {
                //todo add shimmer loading?
                uiState.value.copy(isLoading = false)
            }
        }
    }
}
