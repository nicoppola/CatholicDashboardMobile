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
import navigation.MainComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.theme.LiturgicalColor
import ui.theme.primaryWhite
import ui.theme.secondaryWhite
import ui.theme.tertiaryWhite

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun MainScreen(navComponent: MainComponent) {
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiStatus by viewModel.uiStatus.collectAsState()

    when (uiStatus) {
        MainUiStatus.NavToSettings -> {
            viewModel.clearUiStatus()
            navComponent.onNavSettings()
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
                            onRefresh = { viewModel.retrieveData() },
                            onNavUrl = { url, title -> navComponent.onNavWebView(url, title) })
                    }
                }
            }
        }
    )
}

@Composable
fun MainContent(uiState: MainUiState, onRefresh: () -> Unit, onNavUrl: (String, String) -> Unit) {
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
                if (it.isEnabled) {
                    LinkSection(
                        uiStates = listOf(it),
                        onNavUrl = onNavUrl
                    )
                }
            }
            if (uiState.office.isNotEmpty()) {
                if (uiState.office.find { it.isEnabled } != null) {
                    LinkSection(
                        uiStates = uiState.office,
                        onNavUrl = onNavUrl
                    )
                }
            }
            uiState.officeOfReadings?.let {
                if (it.isEnabled) {
                    LinkSection(
                        uiStates = listOf(it),
                        onNavUrl = onNavUrl
                    )
                }
            }
        }
    }
}

@Composable
fun LinkSection(
    modifier: Modifier = Modifier,
    uiStates: List<ListItemUiState>,
    onNavUrl: (String, String) -> Unit,
) {
    Column(modifier) {
        LinkCardHeader(uiStates.first())
        uiStates.forEach { LinkCard(it, onNavUrl) }
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
    onNavUrl: (String, String) -> Unit,
) {
    if (!uiState.isEnabled) {
        return
    }

    val uriHandler = LocalUriHandler.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        onClick = { onNavUrl(uiState.link, uiState.title) }
        //onClick = { uriHandler.openUri(uiState.link) }
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