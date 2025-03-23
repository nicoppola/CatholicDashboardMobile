package ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coppola.catholic.Res
import com.coppola.catholic.baseline_expand_less_24
import com.coppola.catholic.baseline_expand_more_24
import com.coppola.catholic.baseline_open_in_new_24
import com.coppola.catholic.keyboard_arrow_left
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

@OptIn(KoinExperimentalAPI::class)
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
        onToday = viewModel::onTodayClicked,
        onRefresh = viewModel::retrieveData,
        onNextDate = viewModel::onNextDateButton,
        onPreviousDate = viewModel::onPreviousDateButton,
        onLitHoursButton = viewModel::onLitHoursButton,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    uiState: MainUiState,
    setStatusBarColor: @Composable (Color) -> Unit,
    onSettingsClicked: () -> Unit,
    onToday: () -> Unit,
    onRefresh: () -> Unit,
    onNextDate: () -> Unit,
    onPreviousDate: () -> Unit,
    onLitHoursButton: (Boolean) -> Unit,
) {

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
    setStatusBarColor(MaterialTheme.colorScheme.primary)

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(horizontal = 8.dp),
                navigationIcon = {
                    if (!uiState.isToday) {
                        IconButton(onClick = onToday) {
                            Icon(
                                tint = MaterialTheme.colorScheme.onPrimary,
                                painter = painterResource(uiState.todayIcon),
                                contentDescription = null,
                            )
                        }
                    }
                },
                title = { Text("Catholic Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
//                actions = {
//                    IconButton(onClick = onSettingsClicked) {
//                        Icon(
//                            tint = MaterialTheme.colorScheme.onPrimary,
//                            painter = painterResource(Res.drawable.settings_24),
//                            contentDescription = null,
//                        )
//                    }
//                }
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 18.dp)
            ) {
                PullToRefreshBox(
                    state = pullRefreshState,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier.align(Alignment.TopCenter).zIndex(1F),
                ) {
                    MainContent(
                        uiState = uiState,
                        onNavUrl = { url, _ ->
                            webViewController.open(url = url)
                        },
                        onNextDateButton = onNextDate,
                        onPreviousDateButton = onPreviousDate,
                        onLitHoursButton = onLitHoursButton,
                    )
                }
            }

        }
    )
}


@Composable
fun MainContent(
    uiState: MainUiState,
    onNavUrl: (String, String) -> Unit,
    onPreviousDateButton: () -> Unit,
    onNextDateButton: () -> Unit,
    onLitHoursButton: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Transparent
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {
        // Date
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousDateButton
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    painter = painterResource(Res.drawable.keyboard_arrow_left),
                    contentDescription = null,
                )
            }
            Text(
                modifier = Modifier.weight(1F).fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onPrimary,
                text = uiState.date,
            )

            IconButton(
                onClick = onNextDateButton
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    painter = painterResource(Res.drawable.keyboard_arrow_right),
                    contentDescription = null,
                )
            }
        }

        // Season
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            text = uiState.title
        )

        // Feasts
        if (uiState.feasts.isNotEmpty()) {
            LinkCardHeader(ListItemHeaderUiState(title = "Optional Memorials"))
            uiState.feasts.forEach {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = it.title
                )
            }
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
                    onNavUrl = onNavUrl,
                    onHeaderButton = onLitHoursButton
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
        Spacer(Modifier.weight(1F))
        LinkSection(
            uiStates = listOf(
                ListItemUiState(
                    header = ListItemHeaderUiState(
                        title = "Feedback"
                    ),
                    type = ListItemType.FEEDBACK,
                    isEnabled = true,
                    text = "Report a bug or send me suggestions!",
                    link = "https://forms.gle/SWjRG7xEMgRv6Voq5",

            )),
            onNavUrl = onNavUrl
        )
    }
}

@Composable
fun LinkSection(
    modifier: Modifier = Modifier,
    uiStates: List<ListItemUiState>,
    onNavUrl: (String, String) -> Unit,
    onHeaderButton: (Boolean) -> Unit = {},
) {
    Column(modifier) {
        LinkCardHeader(uiStates.first().header, onHeaderButton)
        uiStates.forEach { LinkCard(it, onNavUrl) }
    }
}

@Composable
fun LinkCardHeader(
    uiState: ListItemHeaderUiState,
    onButton: (Boolean) -> Unit = {},
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleSmall,
            text = uiState.title
        )

        if(uiState.isExpanded != null){
            IconButton(onClick = { onButton(uiState.isExpanded.not()) }) {
                val drawable =
                    if (uiState.isExpanded) Res.drawable.baseline_expand_less_24 else Res.drawable.baseline_expand_more_24
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    painter = painterResource(drawable),
                    contentDescription = null,
                )
            }
        } else {
            Spacer(Modifier.height(40.dp)) // height of icon btn; want to make all spacing consistent
        }
        uiState.isExpanded?.let {

//            TextButton(
//                modifier = Modifier.padding(start = 4.dp),
//                onClick = onButton
//            ) {
//                Text(
//                    style = TextStyle.Default,
//                    text = it,
//                    color = MaterialTheme.colorScheme.onPrimary,
//                )
//            }
        }
    }

}

@Composable
fun LinkCard(
    uiState: ListItemUiState,
    onNavUrl: (String, String) -> Unit,
) {
    if (!uiState.isEnabled) {
        return
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = { onNavUrl(uiState.link, "") }
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