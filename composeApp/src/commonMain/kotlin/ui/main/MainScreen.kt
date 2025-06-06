package ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.SelectableDates
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
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import navigation.MainComponent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.theme.MyDatePickerColors
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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
        selectableDates =
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return viewModel.isDateSelectable(utcTimeMillis)
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return viewModel.isYearSelectable(year)
                }
            }
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
                            datePickerState.selectedDateMillis?.let {
                                viewModel.onDateSelected(it)
                            }
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

    var isMenuExpanded by remember { mutableStateOf(false) }

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

//                    IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
//                        Icon(
//                            tint = MaterialTheme.colorScheme.onPrimary,
//                            painter = painterResource(Res.drawable.baseline_more_vert_24),
//                            contentDescription = null,
//                        )
//                    }
//
//                    DropdownMenu(
//                        expanded = isMenuExpanded,
//                        onDismissRequest = { isMenuExpanded = false }) {
//                        DropdownMenuItem(
//                            leadingIcon = {
//                                Icon(
//                                    tint = MaterialTheme.colorScheme.onPrimary,
//                                    painter = painterResource(Res.drawable.baseline_calendar_today_24),
//                                    contentDescription = null,
//                                )
//                            },
//                            text = { Text("Calendar") },
//                            onClick = {
//                                isMenuExpanded = false
//                                onCalendarClicked()
//                            }
//                        )
//
//                        DropdownMenuItem(
//                            leadingIcon = {
//                                Icon(
//                                    tint = MaterialTheme.colorScheme.onPrimary,
//                                    painter = painterResource(Res.drawable.settings_24),
//                                    contentDescription = null,
//                                )
//                            },
//                            text = { Text("Settings") },
//                            onClick = onSettingsClicked
//                        )
//
//                        DropdownMenuItem(
//                            leadingIcon = {
//                                Icon(
//                                    tint = MaterialTheme.colorScheme.onPrimary,
//                                    painter = painterResource(Res.drawable.baseline_bug_report_24),
//                                    contentDescription = null,
//                                )
//                            },
//                            text = { Text("Report a bug") },
//                            onClick = { webViewController.open(url = "https://forms.gle/wW4zkZmTAyY7rTtFA") }
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
                AnimatedVisibility(
                    visible = uiState.isLoading,
                    enter = fadeIn(animationSpec = tween(durationMillis = 220)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 220)),
                ) {
                    RefreshContent()
                }

                AnimatedVisibility(
                    visible = !uiState.isLoading,
                    enter = fadeIn(animationSpec = tween(durationMillis = 320)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 320)),
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
                onClick = onPreviousDateButton,
                enabled = uiState.canSelectPrevious
            ) {
                if(uiState.canSelectPrevious){
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        painter = painterResource(Res.drawable.keyboard_arrow_left),
                        contentDescription = null,
                    )
                }
            }
            Text(
                modifier = Modifier.weight(1F).fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onPrimary,
                text = uiState.date,
            )

            IconButton(
                onClick = onNextDateButton,
                enabled = uiState.canSelectNext
            ) {
                if(uiState.canSelectNext) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        painter = painterResource(Res.drawable.keyboard_arrow_right),
                        contentDescription = null,
                    )
                }
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

        Spacer(Modifier.padding(bottom = 12.dp))

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
    uiState: ListCollectionUiState,
    onNavUrl: (String, String) -> Unit,
    onHeaderButton: (Boolean) -> Unit = {},
) {
    val filteredItems =
        if (uiState.isExpanded == true) uiState.items else uiState.items.filter { it.showOnCollapsed }
    Column(
        Modifier.wrapContentHeight()
    ) {
        ListHeader(
            text = uiState.header,
            isExpanded = uiState.isExpanded,
            onButton = onHeaderButton
        )
        filteredItems.forEach { item ->
            LinkCard(
                uiState = item,
                onNavUrl = onNavUrl
            )
        }
    }
}

@Composable
fun ListHeader(
    modifier: Modifier = Modifier,
    text: String,
    isExpanded: Boolean? = null,
    onButton: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .semantics(mergeDescendants = true) { heading() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
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
            IconButton(onClick = {}, enabled = false) {}
        }
    }
}

@Composable
fun LinkCard(
    modifier: Modifier = Modifier,
    uiState: ListCollectionItemUiState,
    onNavUrl: (String, String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .then(modifier),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = { uiState.link?.let { onNavUrl(it, "") } }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(Modifier.weight(1F).fillMaxSize(), verticalAlignment = Alignment.Top) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    uiState.subHeader?.let {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleSmall,
                            text = uiState.subHeader,
                        )
                    }
                    uiState.rows.forEach {
                        Row(modifier = Modifier.wrapContentSize()) {
                            val text = buildAnnotatedString {
                                it.title?.let {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Medium,
                                        )
                                    ) {
                                        append(it)
                                    }
                                    append(" ")
                                }
                                it.text?.let { append(it) }
                            }
                            Text(text)
                        }
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