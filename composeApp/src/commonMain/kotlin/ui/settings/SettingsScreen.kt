package ui.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.coppola.catholic.Res
import com.coppola.catholic.arrow_back_24
import navigation.SettingsComponent
import org.coppola.catholic.OfficePrefs
import org.coppola.catholic.TimePref
import org.coppola.catholic.TimeRangePrefs
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.settings.SettingsScreenUiStateOld.SettingUiStateOld

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
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        content = { innerPadding ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.padding(innerPadding)) {
                    LazyColumn {
                        item {
                            Text(
                                text = "Choose which items show on your home screen",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                        item {
                            uiState.readingsSettings?.enabled?.let {
                                SettingsItem(
                                    title = "Daily Readings",
                                    isEnabled = it,
                                    onCheckChanged = { switchState ->
                                        viewModel.onReadingsChanged(
                                            uiState.readingsSettings?.copy(
                                                enabled = switchState
                                            )
                                        )
                                    },
                                )
                            }
                            uiState.liturgyOfHours?.enabled?.let {
                                SettingsItem(
                                    title = "Divine Office",
                                    isEnabled = it,
                                    onCheckChanged = { switchState ->
                                        viewModel.onLiturgyHoursChanged(
                                            uiState.liturgyOfHours?.copy(
                                                enabled = switchState
                                            )
                                        )
                                    },
                                )
                            }
                            uiState.officeOfReadings?.enabled?.let {
                                SettingsItem(
                                    title = "Office of Readings",
                                    isEnabled = it,
                                    onCheckChanged = { switchState ->
                                        viewModel.onOfficeOfReadingsUpdated(
                                            uiState.officeOfReadings?.copy(
                                                enabled = switchState
                                            )
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SettingsItem(
    title: String,
    isEnabled: Boolean,
    onCheckChanged: (Boolean) -> Unit,
    //todo this is so messy.... i think i need to revisit saving preference data to data store instead
    officePrefs: OfficePrefs? = null,
    onOfficeChanged: (OfficePrefs) -> Unit = {}
) {
    var isEditable by remember { mutableStateOf(false) }
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
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1F))
                Switch(
                    checked = isEnabled,
                    onCheckedChange = { onCheckChanged(!it) })
            }
            if (isEnabled && officePrefs != null) {
                TextButton(onClick = { isEditable = !isEditable }) {
                    Text(
                        text = if (!isEditable) "Edit Times" else "Save Times"
                    )
                }
                if (isEditable) {
                    HorizontalDivider(Modifier.padding(top = 8.dp, bottom = 12.dp))

                    //todo put times and labels in here
//                    lauds = getTimeSettingUiState(prefs.lauds, "Morning Prayer"),
//                    prime = null,
//                    terce = getTimeSettingUiState(prefs.terce, "Mid-morning Prayer"),
//                    sext = getTimeSettingUiState(prefs.sext, "Midday Prayer"),
//                    none = getTimeSettingUiState(prefs.none, "Mid-afternoon Prayer"),
//                    vespers = getTimeSettingUiState(prefs.vespers, "Evening Prayer"),
//                    compline = getTimeSettingUiState(prefs.compline, "Night Prayer"),
//                    matins = null,
                    officePrefs.lauds?.let {
                        TimeRow(
                            "Morning Prayer", it
                        ) { newTime ->
                            onOfficeChanged(
                                officePrefs.copy(lauds = newTime)
                            )
                        }
                    }
//                    officePrefs.prime?.let {
//                        TimeRow("?", it) { time ->
//                            onOfficeChanged(
//                                uiState.copy(
//                                    prime = time
//                                )
//                            )
//                        }
//                    }
                    officePrefs.terce?.let {
                        TimeRow("Mid-morning Prayer", it) { time ->
                            onOfficeChanged(
                                officePrefs.copy(
                                    terce = time
                                )
                            )
                        }
                    }
                    officePrefs.sext?.let {
                        TimeRow("Midday Prayer", it) { time ->
                            onOfficeChanged(
                                officePrefs.copy(
                                    terce = time
                                )
                            )
                        }
                    }
                    officePrefs.none?.let {
                        TimeRow("Mid-afternoon Prayer", it) { time ->
                            onOfficeChanged(
                                officePrefs.copy(
                                    sext = time
                                )
                            )
                        }
                    }
                    officePrefs.vespers?.let {
                        TimeRow("Evening Prayer", it) { time ->
                            onOfficeChanged(
                                officePrefs.copy(
                                    vespers = time
                                )
                            )
                        }
                    }
                    officePrefs.compline?.let {
                        TimeRow("Night Prayer", it) { time ->
                            onOfficeChanged(
                                officePrefs.copy(
                                    compline = time
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimeRow(
    tite: String?,
    time: TimeRangePrefs,
    onTimeChanged: (TimeRangePrefs) -> Unit
) {
    val showStartDialog = remember { mutableStateOf(false) }
    val showEndDialog = remember { mutableStateOf(false) }

    if (showStartDialog.value) {
        TimePickerDialog(
            time = TimeUiState(time.startTime?.hour ?: 0, time.startTime?.minute ?: 0),
            onConfirm = { newTime ->
                onTimeChanged(
                    time.copy(
                        startTime = TimePref(newTime.hour, newTime.minute)
                    )
                )
                showStartDialog.value = false
            },
            onDismiss = { showStartDialog.value = false },
        )
    }

    if (showEndDialog.value) {
        TimePickerDialog(
            time = TimeUiState(time.endTime?.hour ?: 0, time.endTime?.minute ?: 0),
            onConfirm = { newTime ->
                onTimeChanged(
                    time.copy(
                        endTime = TimePref(newTime.hour, newTime.minute)
                    )
                )
                showEndDialog.value = false
            },
            onDismiss = { showEndDialog.value = false },
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = tite ?: "",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.weight(1F))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = { showStartDialog.value = true }) {
                Time(
                    time = TimeUiState(time.startTime?.hour ?: 0, time.startTime?.minute ?: 0),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                " - ",
                style = MaterialTheme.typography.bodyLarge
            )
            TextButton(onClick = { showEndDialog.value = true }) {
                Time(
                    time = TimeUiState(time.endTime?.hour ?: 0, time.endTime?.minute ?: 0),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}