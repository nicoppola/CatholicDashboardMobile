package ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.CalendarData
import data.MainRepository
import datastore.PreferencesRepository
import domain.GetOfficeListItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ui.theme.LiturgicalColor
import util.onError
import util.onSuccess

class MainViewModel(
    private val repo: MainRepository,
    private val getOfficeListItemUseCase: GetOfficeListItemUseCase,
) : ViewModel() {

    //private val getOfficeListItemUseCase = GetOfficeListItemUseCase()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _uiStatus = MutableStateFlow<MainUiStatus?>(null)
    val uiStatus: StateFlow<MainUiStatus?> = _uiStatus.asStateFlow()

    init {
        updateFromMyApi()
    }

    fun onSettingsClicked(){
        _uiStatus.update {
            MainUiStatus.NavToSettings
        }
    }

    fun clearUiStatus(){
        _uiStatus.update {
            null
        }
    }

    fun updateFromMyApi() {
        viewModelScope.launch {
            _uiState.update {
                uiState.value.copy(isLoading = true)
            }
            repo.myRetrieveData()
                .onSuccess { data ->
                    println("***** SUCCESS $data")
                    _uiState.update { _ ->
                        uiState.value.copy(
                            date = data.date,
                            title = data.title,
                            color = LiturgicalColor.fromName(data.color.name) ?: LiturgicalColor.GREEN,
                            feasts = data.proper.map { FeastUiState(it.title ?: "") },
                            listObjects = getListItems(data.readings, data.office)
                        )
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
                //todo add shimmer loading
                uiState.value.copy(isLoading = false)
            }
        }
    }

    private suspend fun getListItems(
        readings: CalendarData.Readings,
        office: CalendarData.Office
    ): List<ListObject> {
        val listItems = mutableListOf<ListObject>()
        listItems.add(
            ListObject(
                title = "Daily Readings",
                text =  "Reading 1: ${readings.readingOne}\n" +
                        (if(readings.readingTwo != null) "Reading 2: $readings.readingTwo \n" else "") +
                        "Psalm: ${readings.psalm}\n" +
                        "Gospel: ${readings.gospel}",
                link = readings.link,
            )
        )
        listItems.addAll(getOfficeListItemUseCase(office))
        listItems.add(
            ListObject(
                title = "Office of Readings",
                text = "Office? idk put something here", //todo
                link = office.officeOfReadings
            )
        )
        return listItems
    }
}