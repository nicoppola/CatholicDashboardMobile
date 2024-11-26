package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ui.main.FeastUiState
import ui.main.ListItemType
import ui.main.ListItemUiState
import ui.main.MainContent
import ui.main.MainUiState
import ui.theme.LiturgicalColor

@Preview(showBackground = true)
@Composable
private fun TestTypography() {
    MaterialTheme {
        Column {
            Text(
                text = "Display Large",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "Display Medium",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "Display Small",
                style = MaterialTheme.typography.displaySmall
            )
            HorizontalDivider(Modifier.padding(12.dp))
            Text(
                text = "Headline Large",
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "Headline Medium",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Headline Small",
                style = MaterialTheme.typography.headlineSmall
            )
            HorizontalDivider(Modifier.padding(12.dp))
            Text(
                text = "Title Large",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Title Medium",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Title Small",
                style = MaterialTheme.typography.titleSmall
            )
            HorizontalDivider(Modifier.padding(12.dp))
            Text(
                text = "Label Large",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = "Label Medium",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Label Small",
                style = MaterialTheme.typography.labelSmall
            )
            HorizontalDivider(Modifier.padding(12.dp))
            Text(
                text = "Body Large",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Body Medium",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Body Small",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))

        }
    }
}

@Preview
@Composable
private fun PreviewMain() {
    MaterialTheme {
        MainContent(
            MainUiState(
                date = "October 5, 2024",
                title = "Twenty Sixth Week of Ordinary Time",
                color = LiturgicalColor.GREEN,
                feasts = listOf(
                    FeastUiState("Saint Faustina Kowalska, virgin"),
                    FeastUiState("Blessed Francis Xavier Seelos, Priest")
                ),
                upcoming = emptyList(),
                readings =
                ListItemUiState(
                    isEnabled = true,
                    type = ListItemType.READINGS,
                    title = "Daily Readings",
                    text = "Reading 1: Ez 2:8—3:4\nPsalm: 119:14, 24, 72, 103, 111, 131\nGospel: Matt 18:1-5, 10, 12-14",
                ),
                office =
                listOf(
                    ListItemUiState(
                        isEnabled = true,
                        type = ListItemType.OFFICE,
                        title = "Divine Office",
                        text = "Evening Prayer 4:00p - 6:00p"
                    )
                ),
            ),
            onNavUrl = { _, _ -> },
        )
    }
}