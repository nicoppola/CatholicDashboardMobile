package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import catholicdashboard.composeapp.generated.resources.Res
import catholicdashboard.composeapp.generated.resources.baseline_keyboard_arrow_right_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.theme.CatholicDashboardTheme

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun App() {
    //val dbClient = koinInject<DbClient>()

    CatholicDashboardTheme {
        KoinContext {
            val viewModel = koinViewModel<MainViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Catholic Dashboard") },
                        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = uiState.color.color)
                    )
                },
                content = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(15.dp),
                                strokeWidth = 1.dp,
                                color = Color.Magenta
                            )
                        } else {
                            Button(
                                onClick = { viewModel.update() },
                                content = { Text("Refresh") }
                            )
                            MainContent(uiState)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MainContent(uiState: MainUiState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Column(
                Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                uiState.color.color,
                                uiState.color.color,
                                uiState.color.color,
                                Color.White
                            )
                        )
                    )
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            ) {
                // Date
                Text(
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.headlineSmall,
                    text = uiState.date
                )
                // Season
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium,
                    text = uiState.season
                )

                uiState.feasts.forEach {
                    // Feasts
                    Row(Modifier.padding(bottom = 24.dp)) {
                        Text(
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start,
                            text = it.feast
                        )
                    }
                }

                uiState.upcoming.groupBy { it.title }
                    .forEach {
                        UpcomingRowTitle(it.key)
                        it.value.forEach { j ->
                            UpcomingRow(j.feast)
                        }
                    }
            }
        }
        item {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                uiState.listObjects.forEach {
                    ListItemApp(
                        text = it.headline,
                        subText = it.subText,
                        leadingIcon = painterResource(it.icon),
                        link = it.link
                    )
                }
            }
        }

    }
}

@Composable
fun UpcomingRowTitle(
    text: String,
) {
    Row(Modifier.padding(bottom = 8.dp)) {
        Text(
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            text = text
        )
    }
}

@Composable
fun UpcomingRow(
    text: String,
    link: String? = null,
) {
    Row(Modifier.padding(bottom = 16.dp)) {
        Text(
            modifier = Modifier.weight(9F),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            text = text
        )
        Icon(
            painterResource(Res.drawable.baseline_keyboard_arrow_right_24),
            modifier = Modifier.weight(1F),
            contentDescription = null,
        )

    }
}

@Composable
fun ListItemApp(
    text: String,
    subText: String?,
    leadingIcon: Painter,
    link: String = "",
) {
    val uriHandler = LocalUriHandler.current
    ListItem(
        modifier = Modifier
            .background(Color.Transparent)
            .clickable{
                uriHandler.openUri(link)
            },
        headlineContent = { Text(text = text) },
        supportingContent = subText?.let { { Text(text = subText) } },
        leadingContent = {
            Icon(
                leadingIcon,
                contentDescription = null,
            )
        },
        trailingContent = {
            Icon(
                painterResource(Res.drawable.baseline_keyboard_arrow_right_24),
                contentDescription = null,
            )
        },
    )
}

@Preview
@Composable
private fun PreviewMain() {
    MaterialTheme {
    }
}