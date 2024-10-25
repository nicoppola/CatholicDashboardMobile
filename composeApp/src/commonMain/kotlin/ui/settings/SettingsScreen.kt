package ui.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.main.MainContent
import ui.main.MainViewModel
import ui.theme.LiturgicalColor
import ui.theme.primaryWhite

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
) {
//    val viewModel = koinViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = LiturgicalColor.GREEN.color,
                        titleContentColor = primaryWhite
                    ),
            )
        },
        containerColor = LiturgicalColor.GREEN.color,
        content = { innerPadding ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.padding(innerPadding)) {
                    SettingsContent(uiState)
                }

            }
        }
    )

}

@Composable
fun SettingsContent(
    uiState: SettingsUiState
) {
    LazyColumn {
        items(uiState.settings) {
            Item(it, { _ -> }, { _ -> })
        }
    }
}

@Composable
fun Item(
    uiState: SettingsUiState.Setting,
    onCheckChanged: (String) -> Unit,
    onTimeChanged: (SettingsUiState.TimeSetting) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = uiState.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1F))
                Checkbox(
                    checked = uiState.isChecked,
                    onCheckedChange = { onCheckChanged(uiState.title) })
            }
            if (uiState.isChecked) {
                HorizontalDivider(Modifier.padding(top = 8.dp, bottom = 12.dp))
                uiState.times.forEach {

                    val showStartDialog = remember { mutableStateOf(false) }
                    val showEndDialog = remember { mutableStateOf(false) }

                    if (showStartDialog.value) {
                        TimePickerDialog(
                            time = it.start,
                            onConfirm = { newTime -> onTimeChanged(it.copy(start = newTime)) },
                            onDismiss = { showStartDialog.value = false },
                        )
                    }

                    if (showEndDialog.value) {
                        TimePickerDialog(
                            time = it.start,
                            onConfirm = { newTime -> onTimeChanged(it.copy(end = newTime)) },
                            onDismiss = { showEndDialog.value = false },
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = it.label,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.weight(1F))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            TextButton(onClick = { /*TODO*/ }) {
                                Time(
                                    time = it.start,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            Text(
                                " - ",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            TextButton(onClick = { /*TODO*/ }) {
                                Time(
                                    time = it.end,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}