package ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import catholicdashboard.composeapp.generated.resources.Res
import catholicdashboard.composeapp.generated.resources.baseline_keyboard_arrow_right_24
import catholicdashboard.composeapp.generated.resources.settings_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.theme.LiturgicalColor
import ui.theme.primaryWhite
import ui.theme.secondaryWhite
import ui.theme.tertiaryWhite

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun App(navToSettings: () -> Unit) {
    //val dbClient = koinInject<DbClient>()
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiStatus by viewModel.uiStatus.collectAsState()

    when (uiStatus) {
        MainUiStatus.NavToSettings -> {
            navToSettings()
            viewModel.clearUiStatus()
        }

        else -> {}
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catholic Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = uiState.color.color,
                        titleContentColor = primaryWhite
                    ),
                actions = {
                    IconButton(onClick = viewModel::onSettingsClicked) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(Res.drawable.settings_24),
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        containerColor = LiturgicalColor.GREEN.color,
        content = { innerPadding ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(15.dp),
                        strokeWidth = 1.dp,
                        color = Color.Magenta
                    )
                } else {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainContent(
                            uiState = uiState,
                            onRefresh = { viewModel.retrieveData() })
                    }
                }
            }
        }
    )
}

@Composable
fun MainContent(uiState: MainUiState, onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                uiState.color.color
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        Column(
            Modifier
                .background(
                    uiState.color.color
                )
                .padding(16.dp)
        ) {
            // Date
            Text(
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
                color = primaryWhite,
                text = uiState.date,
            )
            // Season
            Text(
                modifier = Modifier.padding(bottom = 12.dp),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                ),
                color = secondaryWhite,
                text = uiState.title
            )

            // Feasts
            uiState.feasts.forEach {
                Text(
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Start,
                    color = tertiaryWhite,
                    text = it.title
                )
            }

            Spacer(Modifier.padding(bottom = 16.dp))

            uiState.readings?.let {
                LinkSection(
                    uiStates = listOf(it)
                )
            }
            if(uiState.office.isNotEmpty()){
                LinkSection(
                    uiStates = uiState.office
                )
            }
            uiState.officeOfReadings?.let {
                LinkSection(
                    uiStates = listOf(it)
                )
            }
        }
    }
}

@Composable
fun LinkSection(
    modifier: Modifier = Modifier,
    uiStates: List<ListItemUiState>,
) {
    Column(modifier) {
        LinkCardHeader(uiStates.first())
        uiStates.forEach { LinkCard(it) }
    }
}

@Composable
fun LinkCardHeader(
    uiState: ListItemUiState,
) {
    Text(
        modifier = Modifier.padding(vertical = 8.dp),
        color = primaryWhite,
        style = MaterialTheme.typography.titleSmall,
        text = uiState.title
    )
}

@Composable
fun LinkCard(
    uiState: ListItemUiState,
) {
    if (!uiState.isEnabled) {
        return
    }

    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        onClick = { uriHandler.openUri(uiState.link) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = uiState.text ?: "ahhhhh put text here"
            )
            Spacer(modifier = Modifier.weight(1F))
            Icon(
                painter = painterResource(
                    resource = Res.drawable.baseline_keyboard_arrow_right_24
                ),
                contentDescription = null,
            )
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
                    type = ListItemType.READINGS,
                    title = "Daily Readings",
                    text = "Reading 1: Ez 2:8—3:4\nPsalm: 119:14, 24, 72, 103, 111, 131\nGospel: Matt 18:1-5, 10, 12-14",
                ),
                office =
                listOf(
                    ListItemUiState(
                        type = ListItemType.OFFICE,
                        title = "Divine Office",
                        text = "Evening Prayer 4:00p - 6:00p"
                    )
                ),
                isLoading = false
            ),
            onRefresh = {}
        )
    }
}