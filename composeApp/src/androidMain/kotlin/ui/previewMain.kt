package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coppola.catholic.Res
import com.coppola.catholic.arrow_back_24
import com.coppola.catholic.settings_24
import org.jetbrains.compose.resources.painterResource
import ui.main.FeastsUiState
import ui.main.LinkCard
import ui.main.ListCollection
import ui.main.ListCollectionItemUiState
import ui.main.ListCollectionUiState
import ui.main.MainScaffold
import ui.main.MainUiState
import ui.main.TextRow
import ui.theme.DarkColorScheme
import ui.theme.LightColorScheme
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
private fun Test() {
    Row {
        Icon(
            tint = Color.White,
            painter = painterResource(Res.drawable.arrow_back_24),
            contentDescription = null,
        )
        Icon(
            tint = Color.White,
            painter = painterResource(Res.drawable.settings_24),
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainLight() {
    MaterialTheme(colorScheme = LightColorScheme) {
        MainScaffold(
            uiState = MainUiState(
                isToday = false,
                date = "October 5, 2024",
                title = "Twenty Sixth Week of Ordinary Time",
                color = LiturgicalColor.GREEN,
                optionalMemorials = FeastsUiState(
                    title = "Optional Memorials",
                    feasts = listOf(
                        "Saint Faustina Kowalska, virgin",
                        "Blessed Francis Xavier Seelos, Priest"
                    )
                ),
                memorials = null,
                upcoming = null,
                readings =
                ListCollectionUiState(
                    header = "Daily Readings",
                    isExpanded = null,
                    items = listOf(
                        ListCollectionItemUiState(
                            rows = listOf(
                                TextRow("Reading 1:", "Ez 2:8—3:4"),
                                TextRow("Psalm:", "119:14, 24, 72, 103, 111, 131"),
                                TextRow("Gospel:", "Matt 18:1-5, 10, 12-14")
                            )
                        )
                    ),
                ),
                liturgyOfHours =
                ListCollectionUiState(
                    header = "Liturgy of the Hours",
                    isExpanded = false,
                    items = listOf(
                        ListCollectionItemUiState(
                            rows = listOf(
                                TextRow("Evening Prayer", "4:00p - 6:00p"),
                            )
                        )
                    ),
                ),
            ),
            setStatusBarColor = { _ -> },
            onSettingsClicked = {},
            onRefresh = { },
            onNextDate = { },
            onPreviousDate = { },
            onToday = {},
            onLitHoursExpandBtn = {},
            onReadingsExpandBtn = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainDark() {
    MaterialTheme(colorScheme = DarkColorScheme) {
        MainScaffold(
            uiState = MainUiState(
                date = "December 22, 2024",
                title = "Fourth Sunday of Advent",
                color = LiturgicalColor.VIOLET,
                optionalMemorials = FeastsUiState(
                    title = "Optional Memorials",
                    feasts = listOf(
                        "Saint Faustina Kowalska, virgin",
                        "Blessed Francis Xavier Seelos, Priest"
                    )
                ),
                memorials = null,
                upcoming = null,
                readings =
                ListCollectionUiState(
                    header = "Daily Readings",
                    isExpanded = true,
                    items = listOf(
                        ListCollectionItemUiState(
                            subHeader = "Year C Readings",
                            rows = listOf(
                                TextRow("Reading 1:", "Ez 2:8—3:4"),
                                TextRow("Psalm:", "119:14, 24, 72, 103, 111, 131"),
                                TextRow("Gospel:", "Matt 18:1-5, 10, 12-14")
                            ),
                            link = "LINK",
                        ),
                        ListCollectionItemUiState(
                            subHeader = "Scrutenies Year A Readings",
                            rows = listOf(
                                TextRow("Reading 1:", "Ez 2:8—3:4"),
                                TextRow("Psalm:", "119:14, 24, 72, 103, 111, 131"),
                                TextRow("Gospel:", "Matt 18:1-5, 10, 12-14")
                            ),
                            link = "LINK",
                        )
                    ),
                ),
                liturgyOfHours =
                ListCollectionUiState(
                    header = "Liturgy of the Hours",
                    isExpanded = false,
                    items = listOf(
                        ListCollectionItemUiState(
                            rows = listOf(
                                TextRow("Evening Prayer", "4:00p - 6:00p"),
                            ),
                            link = "LINK",
                        )
                    ),
                ),
                officeOfReadings = ListCollectionUiState(
                    header = "Office of Readings",
                    isExpanded = null,
                    items = listOf(
                        ListCollectionItemUiState(
                            rows = listOf(
                                TextRow(null, "Readings for the day."),
                            ),
                            link = "LINK",
                        )
                    ),
                ),
            ),
            setStatusBarColor = { _ -> },
            onSettingsClicked = {},
            onRefresh = { },
            onNextDate = { },
            onPreviousDate = { },
            onToday = {},
            onLitHoursExpandBtn = {},
            onReadingsExpandBtn = {},
        )
    }
}

@Preview
@Composable
private fun PreviewTest() {
    ListCollection(
        uiState = ListCollectionUiState(
            header = "Daily Readings",
            isExpanded = null,
            items = listOf(
                ListCollectionItemUiState(
                    rows = listOf(
                        TextRow("Reading 1:", "Ez 2:8—3:4"),
                        TextRow("Psalm:", "119:14, 24, 72, 103, 111, 131"),
                        TextRow("Gospel:", "Matt 18:1-5, 10, 12-14")
                    )
                )
            ),
        ),
        onNavUrl = { _, _ -> },
        onHeaderButton = { _ -> },
    )
}

@Preview
@Composable
private fun PreviewTestTwo() {
    LinkCard(
        uiState = ListCollectionItemUiState(
            rows = listOf(
                TextRow("Reading 1:", "Ez 2:8—3:4"),
                TextRow("Psalm:", "119:14, 24, 72, 103, 111, 131"),
                TextRow("Gospel:", "Matt 18:1-5, 10, 12-14")
            ),
            link = "LINK",
        ),
        onNavUrl = {_,_ ->}
    )
}


