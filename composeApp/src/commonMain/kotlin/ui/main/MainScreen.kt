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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import androidx.compose.ui.zIndex
import com.coppola.catholic.Res
import com.coppola.catholic.keyboard_arrow_right
import com.coppola.catholic.settings_24
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
import ui.theme.LiturgicalColor
import ui.theme.MartyrColorScheme
import ui.theme.OrdinaryColorScheme
import ui.theme.PenitentialColorScheme
import ui.theme.RoseColorScheme
import ui.theme.SolemnityColorScheme

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun MainScreen(
    navComponent: MainComponent,
    setStatusBarColor: @Composable (Color) -> Unit
) {
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

    MainScaffold(
        uiState = uiState,
        setStatusBarColor = setStatusBarColor,
        onSettingsClicked = viewModel::onSettingsClicked,
        onRefresh = viewModel::retrieveData,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(uiState: MainUiState,
                 setStatusBarColor: @Composable (Color) -> Unit,
                 onSettingsClicked: () -> Unit,
                 onRefresh:() -> Unit,
                 ){

    val pullRefreshState =
        rememberPullToRefreshState(
        )

    val webViewController by rememberWebViewController()
    WebViewPlatform(
        webViewController = webViewController,
        androidSettings = AndroidWebViewModifier
            .urlBarHidingEnabled(true),
        iosSettings = IosWebViewModifier
            .barCollapsingEnabled(true)
    )

    //todo get a provider or something for this
    val colorScheme = when (uiState.color) {
        LiturgicalColor.GREEN -> OrdinaryColorScheme
        LiturgicalColor.VIOLET -> PenitentialColorScheme
        LiturgicalColor.RED -> MartyrColorScheme
        LiturgicalColor.ROSE -> RoseColorScheme
        LiturgicalColor.WHITE -> SolemnityColorScheme
    }

    setStatusBarColor(colorScheme.primary)

    MaterialTheme(colorScheme = colorScheme) {

        Scaffold(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Catholic Dashboard") },
                    colors = TopAppBarDefaults.topAppBarColors()
                        .copy(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                    actions = {
                        IconButton(onClick = onSettingsClicked) {
                            Icon(
                                tint = Color.White,
                                painter = painterResource(Res.drawable.settings_24),
                                contentDescription = null,
                            )
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.primary,
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 18.dp)
//                    .verticalScroll(rememberScrollState()),
                ) {
                    PullToRefreshBox(
                        state = pullRefreshState,
                        isRefreshing = uiState.isRefreshing,
                        onRefresh =  onRefresh,
                        modifier = Modifier.align(Alignment.TopCenter).zIndex(1F),
                    ) {
                        MainContent(
                            uiState = uiState,
                            onNavUrl = { url, title ->
                                webViewController.open(url = url)
                                //navComponent.onNavWebView(url, title)
                            }
                        )
                    }
                }

            }
        )
    }
}


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
            color = MaterialTheme.colorScheme.onPrimary,
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
            color = MaterialTheme.colorScheme.onPrimary,
            text = uiState.title
        )

        // Feasts
        uiState.feasts.forEach {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onPrimary,
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
        color = MaterialTheme.colorScheme.onPrimary,
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

    // set colors? not sure yet
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        onClick = { onNavUrl(uiState.link, uiState.title) }
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
                    resource = Res.drawable.keyboard_arrow_right
                ),
                contentDescription = null,
            )
        }
    }
}