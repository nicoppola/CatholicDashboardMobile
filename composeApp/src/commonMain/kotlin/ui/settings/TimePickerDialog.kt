package ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(time: TimeUiState, onConfirm: (TimeUiState) -> Unit, onDismiss: () -> Unit) {
    val timePickerState = rememberTimePickerState(
        initialHour = time.hour,
        initialMinute = time.minute,
        is24Hour = true,
    )

    BasicAlertDialog(
        modifier = Modifier.width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
            .background(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface
            ),
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "Enter Time",
                style = MaterialTheme.typography.bodyMedium,
                color = TimePickerDefaults.colors().timeSelectorUnselectedContentColor,
            )
            TimeInput(
                state = timePickerState,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
                TextButton(onClick = {
                    onConfirm(
                        TimeUiState(
                            timePickerState.hour,
                            timePickerState.minute,
                        )
                    )
                }) {
                    Text("OK")
                }
            }
        }
    }
}