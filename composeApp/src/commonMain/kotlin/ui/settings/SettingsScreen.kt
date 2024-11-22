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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import catholicdashboard.composeapp.generated.resources.Res
import catholicdashboard.composeapp.generated.resources.arrow_back_24
import navigation.SettingsComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.settings.SettingsScreenUiState.SettingUiState
import ui.theme.LiturgicalColor
import ui.theme.primaryWhite

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navComponent: SettingsComponent,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = navComponent::onBack) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(Res.drawable.arrow_back_24),
                            contentDescription = null,
                        )
                    }
                },
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
                    SettingsContent(uiState, viewModel::onCheckChanged, viewModel::onTimeChanged)
                }

            }
        }
    )

}

@Composable
fun SettingsContent(
    uiState: SettingsScreenUiState,
    onCheckChanged: (SettingUiState) -> Unit,
    onTimeChanged: (SettingUiState) -> Unit,
) {
    LazyColumn {
        items(uiState.settings) {
            Item(it, onCheckChanged, onTimeChanged)
        }
    }
}

@Composable
fun Item(
    uiState: SettingUiState,
    onCheckChanged: (SettingUiState) -> Unit,
    onTimeChanged: (SettingUiState) -> Unit,
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
                    onCheckedChange = { onCheckChanged(uiState) })
            }
            if (uiState.isChecked) {
                when (uiState) {
                    is SettingsScreenUiState.DivineOfficeSettingUiState -> {
                        HorizontalDivider(Modifier.padding(top = 8.dp, bottom = 12.dp))
                        uiState.lauds?.let { TimeRow(it) { time -> onTimeChanged(uiState.copy(lauds = time)) } }
                        uiState.prime?.let { TimeRow(it) { time -> onTimeChanged(uiState.copy(prime = time)) } }
                        uiState.terce?.let { TimeRow(it) { time -> onTimeChanged(uiState.copy(terce = time)) } }
                        uiState.sext?.let { TimeRow(it) { time -> onTimeChanged(uiState.copy(terce = time)) } }
                        uiState.none?.let { TimeRow(it) { time -> onTimeChanged(uiState.copy(sext = time)) } }
                        uiState.vespers?.let {
                            TimeRow(it) { time ->
                                onTimeChanged(
                                    uiState.copy(
                                        vespers = time
                                    )
                                )
                            }
                        }
                        uiState.compline?.let {
                            TimeRow(it) { time ->
                                onTimeChanged(
                                    uiState.copy(
                                        compline = time
                                    )
                                )
                            }
                        }
                    }

                    else -> { /* do nothing */
                    }
                }
            }
        }
    }
}


@Composable
fun TimeRow(
    time: SettingsScreenUiState.TimeSettingUiState,
    onTimeChanged: (SettingsScreenUiState.TimeSettingUiState) -> Unit
) {
    val showStartDialog = remember { mutableStateOf(false) }
    val showEndDialog = remember { mutableStateOf(false) }

    if (showStartDialog.value) {
        TimePickerDialog(
            time = time.start,
            onConfirm = { newTime ->
                onTimeChanged(time.copy(start = newTime))
                showStartDialog.value = false
            },
            onDismiss = { showStartDialog.value = false },
        )
    }

    if (showEndDialog.value) {
        TimePickerDialog(
            time = time.end,
            onConfirm = { newTime ->
                onTimeChanged(time.copy(end = newTime))
                showEndDialog.value = false
            },
            onDismiss = { showEndDialog.value = false },
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = time.label,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.weight(1F))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = { showStartDialog.value = true }) {
                Time(
                    time = time.start,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                " - ",
                style = MaterialTheme.typography.bodyLarge
            )
            TextButton(onClick = { showEndDialog.value = true }) {
                Time(
                    time = time.end,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}