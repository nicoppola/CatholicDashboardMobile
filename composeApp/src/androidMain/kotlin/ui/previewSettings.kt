package ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ui.settings.SettingsContent
import ui.settings.SettingsUiState
import ui.settings.Time
import ui.settings.TimePickerDialog

@Composable
@Preview(showBackground = true)
fun TimePreview() {
    TimePickerDialog(
        time = Time(hour = 7, minute = 30),
        onConfirm = { _ -> },
        onDismiss = {}
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewPreviewSettings() {
    MaterialTheme {
        SettingsContent(
            SettingsUiState(
                settings = listOf(
                    SettingsUiState.Setting(
                        title = "Liturgy of the Hours",
                        times = listOf(
                            SettingsUiState.TimeSetting(
                                label = "Morning Prayer",
                                start = Time(hour = 0, minute = 0),
                                end = Time(hour = 7, minute = 30)
                            ),
                            SettingsUiState.TimeSetting(
                                label = "Midmorning Prayer",
                                start = Time(hour = 7, minute = 30),
                                end = Time(hour = 10, minute = 30)
                            )
                        )
                    ),
                    SettingsUiState.Setting(
                        title = "Angelus",
                        isChecked = false,
                        times = listOf(
                            SettingsUiState.TimeSetting(
                                label = "Time",
                                start = Time(hour = 11, minute = 30),
                                end = Time(hour = 12, minute = 30)
                            )
                        )
                    )
                )
            )
        )
    }
}