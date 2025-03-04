package ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import datastore.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.coppola.catholic.OfficeOfReadingsPrefs
import org.coppola.catholic.OfficePrefs
import org.coppola.catholic.ReadingsPrefs
import ui.settings.SettingsScreenUiStateOld.SettingUiStateOld

class SettingsViewModel(private val preferencesRepository: PreferencesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsScreenUiState())
    val uiState: StateFlow<SettingsScreenUiState> = _uiState.asStateFlow()


    //todo this is all so messy, pls fix it later

    init {
        updateSettingsUiState()
    }

    ///// Click Handlers /////
    fun onReadingsChanged(newPrefs: ReadingsPrefs?) {
        viewModelScope.launch {
            newPrefs?.let { preferencesRepository.updateReadings(it) }
        }
    }

    fun onLiturgyHoursChanged(newPrefs: OfficePrefs?) {
        viewModelScope.launch {
            newPrefs?.let { preferencesRepository.updateOffice(it) }
        }
    }

    fun onOfficeOfReadingsUpdated(newPrefs: OfficeOfReadingsPrefs?) {
        viewModelScope.launch {
            newPrefs?.let { preferencesRepository.updateOfficeOfReadings(it) }
        }
    }

    private fun updateSettingsUiState() {
        viewModelScope.launch {
            _uiState.update {
                uiState.value.copy(
                    readingsSettings = getReadingsUiState(),
                    liturgyOfHours = getOfficeUiState(),
                    officeOfReadings = getOfficeOfReadingsUiState()
                )
            }
        }
    }

    private suspend fun getReadingsUiState(): ReadingsPrefs {
        return preferencesRepository.getReadings().first()
    }

    private suspend fun getOfficeUiState(): OfficePrefs {
        return preferencesRepository.getOffice().first()
//        return SettingsScreenUiStateOld.DivineOfficeSettingUiState(
//            title = "Divine Office",
//            isOn = prefs.enabled,
//            lauds = getTimeSettingUiState(prefs.lauds, "Morning Prayer"),
//            prime = null,
//            terce = getTimeSettingUiState(prefs.terce, "Mid-morning Prayer"),
//            sext = getTimeSettingUiState(prefs.sext, "Midday Prayer"),
//            none = getTimeSettingUiState(prefs.none, "Mid-afternoon Prayer"),
//            vespers = getTimeSettingUiState(prefs.vespers, "Evening Prayer"),
//            compline = getTimeSettingUiState(prefs.compline, "Night Prayer"),
//            matins = null,
//        )
    }

    private suspend fun getOfficeOfReadingsUiState(): OfficeOfReadingsPrefs {
        return preferencesRepository.getOfficeOfReadings().first()
    }

}

private fun List<SettingUiStateOld>.replace(copy: SettingUiStateOld): List<SettingUiStateOld> {
    val test = this.toMutableList()
    val curr = this.find { it.title == copy.title }
    val index = this.indexOf(curr)
    test[index] = copy
    return test
}

//private fun <SettingUiState> List<SettingUiState>.replace(
//    newState: SettingsScreenUiState.SettingUiState,
//    type: SettingUiState
//): List<SettingUiState> {
//    this.find { it is type }
//
//}
