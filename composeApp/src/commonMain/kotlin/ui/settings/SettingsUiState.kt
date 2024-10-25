package ui.settings

data class SettingsUiState(
    val settings: List<Setting> = emptyList()
){
    data class Setting(
        val title: String = "",
        val isChecked: Boolean = true,
        val times: List<TimeSetting> = emptyList(),
    )
    data class TimeSetting(
        val label: String = "",
        val start: Time,
        val end: Time,
    )
}

//todo future support meridian time - use what user's OS setting is
data class Time(
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