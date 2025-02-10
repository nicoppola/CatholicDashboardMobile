package ui.settings

data class SettingsScreenUiState(
    val settings: List<SettingUiState> = emptyList()
) {
    sealed class SettingUiState(
        val type: SettingType,
        open val title: String = "",
        open val isChecked: Boolean = true,
        //val times: List<TimeSetting> = emptyList(),
    )

    data class TimeSettingUiState(
        val label: String = "",
        val start: TimeUiState,
        val end: TimeUiState,
    )

    data class ReadingsSettingUiState(
        override val title: String,
        override val isChecked: Boolean,
    ) : SettingUiState(type = SettingType.READINGS)

    data class OfficeOfReadingsSettingUiState(
        override val title: String,
        override val isChecked: Boolean,
    ) :
        SettingUiState(type = SettingType.OFFICE_OF_READINGS)

    data class DivineOfficeSettingUiState(
        override val title: String,
        override val isChecked: Boolean,
        val isEditMode: Boolean = false,
        val lauds: TimeSettingUiState?,
        val prime: TimeSettingUiState?,
        val terce: TimeSettingUiState?,
        val sext: TimeSettingUiState?,
        val none: TimeSettingUiState?,
        val vespers: TimeSettingUiState?,
        val compline: TimeSettingUiState?,
        val matins: TimeSettingUiState?,
    ) : SettingUiState(type = SettingType.DIVINE_OFFICE)


}

enum class TimeSetting {
    LAUDS,
    PRIME,
    TERCE,
    SEXT,
    NONE,
    VESPERS,
    COMPLINE,
    MATINS,
}

enum class SettingType {
    READINGS,
    OFFICE_OF_READINGS,
    DIVINE_OFFICE,
}


//todo future support meridian time - use what user's OS setting is
data class TimeUiState(
    val hour: Int,
    val minute: Int,
)

// Readings   on/off

// Office        on/off
// morning       0000 - 0730
// mid-morning   0730 - 1030
// midday        1030 - 1330
// mid-afternoon 1330 - 1630
// evening       1630 - 1930
// night         1930 - 0000

// Office of Readings   on/off

// Angelus      on/off
// Time         1100 - 1300

// Divine Mercy Prayer   on/pff
// Time                  1400 - 1600