package ui.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

//todo future support meridian time - use what user's OS setting is
@Composable
fun Time(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    time: TimeUiState
) {
    val displayTime = time.hour.toString().padStart(2, '0') +
            time.minute.toString().padStart(2, '0')
    Text(
        modifier = modifier,
        style = style,
        text = displayTime
    )
}