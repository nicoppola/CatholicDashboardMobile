package ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import catholicdashboard.composeapp.generated.resources.Res
import catholicdashboard.composeapp.generated.resources.baseline_keyboard_arrow_right_24
import catholicdashboard.composeapp.generated.resources.settings_24
import com.final_class.webview_multiplatform_mobile.webview.WebViewPlatform
import com.final_class.webview_multiplatform_mobile.webview.controller.rememberWebViewController
import com.final_class.webview_multiplatform_mobile.webview.settings.android.AndroidWebViewModifier
import com.final_class.webview_multiplatform_mobile.webview.settings.android.urlBarHidingEnabled
import com.final_class.webview_multiplatform_mobile.webview.settings.ios.IosWebViewModifier
import com.final_class.webview_multiplatform_mobile.webview.settings.ios.barCollapsingEnabled
import navigation.MainComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.theme.primaryWhite
import ui.theme.secondaryWhite
import ui.theme.tertiaryWhite

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun MainScreen(navComponent: MainComponent) {
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val uiStatus by viewModel.uiStatus.collectAsState()

    val pullRefreshState =
        rememberPullToRefreshState()

    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            viewModel.retrieveData()
        }
    }

    val webViewController by rememberWebViewController()
    WebViewPlatform(
        webViewController = webViewController,
        androidSettings = AndroidWebViewModifier
            .urlBarHidingEnabled(true),
        iosSettings = IosWebViewModifier
            .barCollapsingEnabled(true)
//            .entersReaderIfAvailable(true)
    )

    when (uiStatus) {
        MainUiStatus.NavToSettings -> {
            viewModel.clearUiStatus()
            navComponent.onNavSettings()
        }

        MainUiStatus.LoadingComplete -> {
            viewModel.clearUiStatus()
            pullRefreshState.endRefresh()
        }

        else -> {}
    }
    val backgroundColor = uiState.color.color

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(backgroundColor)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .nestedScroll(pullRefreshState.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catholic Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = backgroundColor,
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
        containerColor = backgroundColor,
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp)
//                    .verticalScroll(rememberScrollState()),
            ) {
                PullToRefreshContainer(
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
                MainContent(
                    uiState = uiState,
                    onNavUrl = { url, title ->
                        webViewController.open(url = url)
                        //navComponent.onNavWebView(url, title)
                    }
                )
            }

        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    uiState: MainUiState,
    onNavUrl: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                //uiState.color.color
                Color.Transparent
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
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

//    val uriHandler = LocalUriHandler.current
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
                modifier = Modifier.weight(1F),
                text = uiState.text ?: "ahhhhh put text here"
            )
            Icon(
                painter = painterResource(
                    resource = Res.drawable.baseline_keyboard_arrow_right_24
                ),
                contentDescription = null,
            )
        }
    }
}