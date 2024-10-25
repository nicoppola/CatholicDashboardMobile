package ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.theme.CatholicDashboardTheme
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
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .size(24.dp)
                        )
//                                Icon(
//                                    painter = painterResource()
//                                )
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
                            onRefresh = { viewModel.updateFromMyApi() })
                    }
                }
            }
        }
    )
}

@Composable
fun MainContent(uiState: MainUiState, onRefresh: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                uiState.color.color
            ),
        horizontalAlignment = Alignment.Start
    ) {
        item {
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

                LinkCard(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "Daily Readings",
                    subText = "Reading 1: Ez 2:8—3:4\nPsalm: 119:14, 24, 72, 103, 111, 131\nGospel: Matt 18:1-5, 10, 12-14"
                )
                LinkCard(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = "Divine Office",
                    subText = "Evening Prayer 4:00p - 6:00p"
                )
            }
        }
    }
}

//TODO on click animation
@Composable
fun LinkCard(
    modifier: Modifier = Modifier,
    text: String,
    subText: String,
    link: String = "",
) {
    val uriHandler = LocalUriHandler.current

    Column {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            color = primaryWhite,
            style = MaterialTheme.typography.titleSmall,
            text = text
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        uriHandler.openUri(link)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subText
                )
//                Spacer(modifier = Modifier.weight(1F))
//                Icon(
//                    painter = painterResource(
//                        id = R.drawable.baseline_keyboard_arrow_right_24
//                    ), contentDescription = null
//                )

            }
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
                listObjects = listOf(
                    ListObject(
                        title = "Daily Readings",
                        text = "Reading 1: Ez 2:8—3:4\nPsalm: 119:14, 24, 72, 103, 111, 131\nGospel: Matt 18:1-5, 10, 12-14",
                    ),
                    ListObject(title = "Divine Office", text = "Evening Prayer 4:00p - 6:00p")
                ),
                isLoading = false
            ),
            onRefresh = {}
        )
    }
}