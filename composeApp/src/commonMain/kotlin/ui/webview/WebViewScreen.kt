package ui.webview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import catholicdashboard.composeapp.generated.resources.Res
import catholicdashboard.composeapp.generated.resources.arrow_back_24
import catholicdashboard.composeapp.generated.resources.baseline_open_in_new_24
import com.final_class.webview_multiplatform_mobile.webview.WebViewPlatform
import com.final_class.webview_multiplatform_mobile.webview.controller.rememberWebViewController
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewState
import navigation.WebViewComponent
import org.jetbrains.compose.resources.painterResource
import ui.theme.LiturgicalColor
import ui.theme.primaryWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
    navComponent: WebViewComponent,
) {
    //todo loading screen
    val uriHandler = LocalUriHandler.current

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(navComponent.title) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = navComponent::onBack) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(Res.drawable.arrow_back_24),
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { uriHandler.openUri(navComponent.url) }) {
                        Icon(
                            tint = Color.White,
                            painter = painterResource(Res.drawable.baseline_open_in_new_24),
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
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = LiturgicalColor.GREEN.color,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
//                    .verticalScroll(rememberScrollState())
                // scroll away doesn't work with iOS :((((((
            ) {
                val state: WebViewState = rememberWebViewState(url = navComponent.url)
                //todo add "back" for to previous screen?
                //todo make status bar and bottom bar transparent here
                //todo add "open in browser" action
                WebView(
                    state = state,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    )
}

