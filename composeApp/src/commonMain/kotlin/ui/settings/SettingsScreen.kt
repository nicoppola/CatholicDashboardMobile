package ui.settings

import androidx.compose.material3.CenterAlignedTopAppBar
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
import com.coppola.catholic.Res
import com.coppola.catholic.arrow_back_24
import navigation.SettingsComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

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
                            tint = MaterialTheme.colorScheme.onPrimary,
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
//            SettingsContentOld(innerPadding, uiState, viewModel)

            //todo - radio buttons for dark/light/system; save in system prefs
        }
    )
}