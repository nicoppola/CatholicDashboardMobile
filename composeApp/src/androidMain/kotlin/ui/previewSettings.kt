package ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ui.settings.SettingsContent
import ui.settings.SettingsScreenUiState
import ui.settings.TimeUiState
import ui.settings.TimePickerDialog

@Composable
@Preview(showBackground = true)
fun TimePreview() {
    TimePickerDialog(
        time = TimeUiState(hour = 7, minute = 30),
        onConfirm = { _ -> },
        onDismiss = {}
    )
}

//@Composable
//@Preview(showBackground = true)
//fun PreviewPreviewSettings() {
//    MaterialTheme {
//        SettingsContent(
//            SettingsScreenUiState(
//                settings = listOf(
//                    SettingsScreenUiState.SettingUiState(
//                        title = "Liturgy of the Hours",
//                        times = listOf(
//                            SettingsScreenUiState.TimeSettingUiState(
//                                label = "Morning Prayer",
//                                start = TimeUiState(hour = 0, minute = 0),
//                                end = TimeUiState(hour = 7, minute = 30)
//                            ),
//                            SettingsScreenUiState.TimeSettingUiState(
//                                label = "Midmorning Prayer",
//                                start = TimeUiState(hour = 7, minute = 30),
//                                end = TimeUiState(hour = 10, minute = 30)
//                            )
//                        )
//                    ),
//                    SettingsScreenUiState.SettingUiState(
//                        title = "Angelus",
//                        isChecked = false,
//                        times = listOf(
//                            SettingsScreenUiState.TimeSettingUiState(
//                                label = "Time",
//                                start = TimeUiState(hour = 11, minute = 30),
//                                end = TimeUiState(hour = 12, minute = 30)
//                            )
//                        )
//                    )
//                )
//            )
//        )
//    }
//}