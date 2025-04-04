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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.coppola.catholic.Res
import com.coppola.catholic.baseline_calendar_today_24
import com.coppola.catholic.baseline_expand_less_24
import com.coppola.catholic.baseline_expand_more_24
import com.coppola.catholic.keyboard_arrow_left
import com.coppola.catholic.keyboard_arrow_right
import com.final_class.webview_multiplatform_mobile.webview.WebViewPlatform
import com.final_class.webview_multiplatform_mobile.webview.controller.rememberWebViewController
import com.final_class.webview_multiplatform_mobile.webview.settings.android.AndroidWebViewModifier
import com.final_class.webview_multiplatform_mobile.webview.settings.android.urlBarHidingEnabled
import com.final_class.webview_multiplatform_mobile.webview.settings.ios.IosWebViewModifier
import com.final_class.webview_multiplatform_mobile.webview.settings.ios.barCollapsingEnabled
import kotlinx.datetime.LocalDate
import navigation.MainComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.theme.MyDatePickerColors
import kotlin.time.DurationUnit
import kotlin.time.toDuration

import androidx.lifecycle.compose.LocalLifecycleOwner

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navComponent: MainComponent,
    setStatusBarColor: @Composable (Color) -> Unit,
) {
    val viewModel = koinViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val navStatus by viewModel.navStatus.collectAsState()

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.onResume()
                }

                else -> {}
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        yearRange = 2025..2025,
    )

    when (navStatus) {
        MainNavStatus.NavToSettings -> {
            viewModel.clearUiStatus()
            navComponent.onNavSettings()
        }

        else -> {}
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MainScaffold(
            uiState = uiState,
            setStatusBarColor = setStatusBarColor,
            onSettingsClicked = viewModel::onSettingsClicked,
            onToday = viewModel::onTodayClicked,
            onRefresh = viewModel::retrieveData,
            onNextDate = viewModel::onNextDateButton,
            onPreviousDate = viewModel::onPreviousDateButton,
            onLitHoursExpandBtn = viewModel::onLitHoursExpandButton,
            onReadingsExpandBtn = viewModel::onReadingsExpandButton,
            onCalendarClicked = { showDatePicker = true },
        )
        if (showDatePicker) {
            datePickerState.selectedDateMillis = uiState.currLocalDate.toEpochDays()
                .toDuration(DurationUnit.DAYS).inWholeMilliseconds
            DatePickerDialog(
                colors = MyDatePickerColors.colors(),
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(
                        onClick = {
                            val date = datePickerState.selectedDateMillis?.let {
                                val days = it.toDuration(DurationUnit.MILLISECONDS)
                                    .inWholeDays
                                    .toInt()

                                LocalDate.fromEpochDays(days)
                            }
                            viewModel.onDateSelected(date)
                            showDatePicker = false
                        }
                    ) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDatePicker = false }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(
                    colors = MyDatePickerColors.colors(),
                    state = datePickerState,
                    showModeToggle = false,
                )
            }
        }

    }


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
    onLitHoursExpandBtn: (Boolean) -> Unit,
    onReadingsExpandBtn: (Boolean) -> Unit,
    onCalendarClicked: () -> Unit = {},
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
                actions = {
                    IconButton(onClick = { onCalendarClicked() }) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onPrimary,
                            painter = painterResource(Res.drawable.baseline_calendar_today_24),
                            contentDescription = null,
                        )
                    }

//                    IconButton(onClick = onSettingsClicked) {
//                        Icon(
//                            tint = MaterialTheme.colorScheme.onPrimary,
//                            painter = painterResource(Res.drawable.settings_24),
//                            contentDescription = null,
//                        )
//                    }
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
                        onLitHoursButton = onLitHoursExpandBtn,
                        onReadingsExpandBtn = onReadingsExpandBtn,
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
    onReadingsExpandBtn: (Boolean) -> Unit,
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

        uiState.optionalMemorials?.let {
            FeastsSection(it)
        }

        Spacer(Modifier.padding(bottom = 16.dp))

        uiState.readings?.let {
           ListCollection(
               uiState = it,
               onNavUrl = onNavUrl,
               onHeaderButton = onReadingsExpandBtn,
           )
        }
        uiState.liturgyOfHours?.let {
            ListCollection(
                uiState = it,
                onNavUrl = onNavUrl,
                onHeaderButton = onLitHoursButton,
            )
        }
        uiState.officeOfReadings?.let {
            ListCollection(
                uiState = it,
                onNavUrl = onNavUrl,
                onHeaderButton = {},
            )
        }
        Spacer(Modifier.weight(1F))
//        ListCollection(
//            uiStates = listOf(
//                ListItemUiState(
//                    header = ListItemHeaderUiState(
//                        title = "Feedback"
//                    ),
//                    type = ListItemType.FEEDBACK,
//                    isEnabled = true,
//                    text = "Report a bug or send me suggestions!",
//                    link = "https://forms.gle/SWjRG7xEMgRv6Voq5",
//
//                    )
//            ),
//            onNavUrl = onNavUrl
//        )
    }
}

@Composable
fun FeastsSection(
    uiState: FeastsUiState,
) {
    ListHeader(text = uiState.title)
    uiState.feasts.forEach {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimary,
            text = it
        )
    }
}

@Composable
fun ListCollection(
    modifier: Modifier = Modifier,
    uiState: ListCollectionUiState,
    onNavUrl: (String, String) -> Unit,
    onHeaderButton: (Boolean) -> Unit = {},
) {
    val filteredItems = if(uiState.isExpanded == true) uiState.items else listOf(uiState.items.first())
    Column(modifier) {
        ListHeader(
            uiState.header,
            uiState.isExpanded,
            onHeaderButton)
        filteredItems.forEach { LinkCard(it, onNavUrl) }
    }
}

@Composable
fun ListHeader(
    text: String,
    isExpanded: Boolean? = null,
    onButton: (Boolean) -> Unit = {},
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleSmall,
            text = text
        )

        if (isExpanded != null) {
            IconButton(onClick = { onButton(isExpanded.not()) }) {
                val drawable =
                    if (isExpanded) Res.drawable.baseline_expand_less_24 else Res.drawable.baseline_expand_more_24
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    painter = painterResource(drawable),
                    contentDescription = null,
                )
            }
        } else {
            Spacer(Modifier.height(40.dp)) // height of icon btn; want to make all spacing consistent
        }
    }

}

@Composable
fun LinkCard(
    uiState: ListCollectionItemUiState,
    onNavUrl: (String, String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = { uiState.link?.let { onNavUrl(it, "") } }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            uiState.rows.forEach {
                Row(modifier = Modifier.weight(1F)){
                    it.title?.let { title ->
                        Text(
                            text = title,
                            style = TextStyle.Default.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    it.text?.let { text ->
                        Text(
                            text = text,
                        )
                    }
                }

            }

            uiState.link?.let {
                Icon(
                    painter = painterResource(
                        resource = Res.drawable.keyboard_arrow_right
                    ),
                    contentDescription = null,
                )
            }
        }
    }
}