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
import org.coppola.catholic.TimePref
import org.coppola.catholic.TimeRangePrefs
import ui.settings.SettingsScreenUiState.SettingUiState
import kotlin.reflect.KClass

class SettingsViewModel(private val preferencesRepository: PreferencesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsScreenUiState())
    val uiState: StateFlow<SettingsScreenUiState> = _uiState.asStateFlow()


    //todo this is all so messy, pls fix it later

    init {
        updateSettingsUiState()
    }

    ///// Click Handlers /////
    fun onCheckChanged(settingUiState: SettingUiState) {

        viewModelScope.launch {
            when (settingUiState) {
                is SettingsScreenUiState.ReadingsSettingUiState -> {
                    preferencesRepository.updateReadings(
                        ReadingsPrefs(!settingUiState.isChecked)
                    )
                    updateSettingUiState(settingUiState.copy(isChecked = !settingUiState.isChecked))
                }

                is SettingsScreenUiState.DivineOfficeSettingUiState -> {
                    preferencesRepository.updateOffice(
                        settingUiState.toOfficePrefs
                            ().copy(!settingUiState.isChecked)
                    )

                    updateSettingUiState(settingUiState.copy(isChecked = !settingUiState.isChecked))
                }

                is SettingsScreenUiState.OfficeOfReadingsSettingUiState -> {
                    preferencesRepository.updateOfficeOfReadings(
                        OfficeOfReadingsPrefs(!settingUiState.isChecked)
                    )
                    updateSettingUiState(settingUiState.copy(isChecked = !settingUiState.isChecked))
                }
            }
        }
    }

    fun onTimeChanged(
        newUiState: SettingUiState
    ) {
        viewModelScope.launch {
            when (newUiState) {
                is SettingsScreenUiState.DivineOfficeSettingUiState -> {
                    preferencesRepository.updateOffice(
                        newUiState.toOfficePrefs()
                    )
                    updateSettingUiState(newUiState)
                }

                else -> {/* do nothing */
                }
            }
        }
    }

    ///// UI State Setup /////

    private fun updateSettingUiState(newUiState: SettingUiState) {
        val newScreenUiState =
            uiState.value.settings.replace(newUiState)
        _uiState.update {
            uiState.value.copy(
                settings = newScreenUiState
            )
        }
    }

    private fun SettingsScreenUiState.DivineOfficeSettingUiState.toOfficePrefs(): OfficePrefs {
        return OfficePrefs(
            enabled = this.isChecked,
            lauds = lauds?.toTimeRangePrefs(),
            prime = prime?.toTimeRangePrefs(),
            terce = terce?.toTimeRangePrefs(),
            sext = sext?.toTimeRangePrefs(),
            none = none?.toTimeRangePrefs(),
            vespers = vespers?.toTimeRangePrefs(),
            compline = compline?.toTimeRangePrefs(),
            matins = matins?.toTimeRangePrefs(),
        )
    }

    private fun SettingsScreenUiState.TimeSettingUiState.toTimeRangePrefs(): TimeRangePrefs {
        return TimeRangePrefs(
            startTime = TimePref(this.start.hour, this.start.minute),
            endTime = TimePref(this.end.hour, this.end.minute),
        )
    }

    private fun getTimeSettingUiState(
        time: TimeRangePrefs?,
        label: String
    ): SettingsScreenUiState.TimeSettingUiState? {
        return if (time != null) {
            SettingsScreenUiState.TimeSettingUiState(
                label = label,
                start = TimeUiState(
                    hour = time.startTime?.hour ?: 0,
                    minute = time.startTime?.minute ?: 0,
                ),
                end = TimeUiState(
                    hour = time.endTime?.hour ?: 0,
                    minute = time.endTime?.minute ?: 0
                )
            )
        } else {
            null
        }
    }

    private fun updateSettingsUiState() {
        viewModelScope.launch {
            _uiState.update {
                uiState.value.copy(
                    settings = listOf(
                        getReadingsUiState(), getOfficeUiState(), getOfficeOfReadingsUiState()
                    )
                )
            }
        }
    }

    private suspend fun getReadingsUiState(): SettingsScreenUiState.ReadingsSettingUiState {
        val prefs = preferencesRepository.getReadings().first()
        return SettingsScreenUiState.ReadingsSettingUiState(
            title = "Daily Readings",
            isChecked = prefs.enabled,
        )
    }

    private suspend fun getOfficeUiState(): SettingsScreenUiState.DivineOfficeSettingUiState {
        val prefs = preferencesRepository.getOffice().first()
        return SettingsScreenUiState.DivineOfficeSettingUiState(
            title = "Divine Office",
            isChecked = prefs.enabled,
            lauds = getTimeSettingUiState(prefs.lauds, "Morning Prayer"),
            prime = null,
            terce = getTimeSettingUiState(prefs.terce, "Mid-morning Prayer"),
            sext = getTimeSettingUiState(prefs.sext, "Midday Prayer"),
            none = getTimeSettingUiState(prefs.none, "Mid-afternoon Prayer"),
            vespers = getTimeSettingUiState(prefs.vespers, "Evening Prayer"),
            compline = getTimeSettingUiState(prefs.compline, "Night Prayer"),
            matins = null,
        )
    }

    private suspend fun getOfficeOfReadingsUiState(): SettingsScreenUiState.OfficeOfReadingsSettingUiState {
        val prefs = preferencesRepository.getOfficeOfReadings().first()
        return SettingsScreenUiState.OfficeOfReadingsSettingUiState(
            title = "Office of Readings",
            isChecked = prefs.enabled,
        )
    }

}

private fun List<SettingUiState>.replace(copy: SettingUiState): List<SettingUiState> {
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
