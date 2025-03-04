package ui.settings

import org.coppola.catholic.OfficeOfReadingsPrefs
import org.coppola.catholic.OfficePrefs
import org.coppola.catholic.ReadingsPrefs


data class SettingsScreenUiState(
    val readingsTitle: String? = null,
    val readingsSettings: ReadingsPrefs? = null,
    val liturgyOfHoursTitle: String? = null,
    val liturgyOfHours: OfficePrefs? = null,
    val officeOfReadingsTitle: String? = null,
    val officeOfReadings: OfficeOfReadingsPrefs? = null,
)
