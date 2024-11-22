package ui.webview

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import catholicdashboard.composeapp.generated.resources.Res
import catholicdashboard.composeapp.generated.resources.arrow_back_24
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator
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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

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
            Box(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                WebViewContent(navComponent.url)
            }
        }
    )
}

@Composable
fun WebViewContent(url: String) {

    val state: WebViewState = rememberWebViewState(url = url)

    //todo add "back" for to preious screen?
    //todo make status bar and bottom bar transparent here
    WebView(
        state = state,
        modifier = Modifier.fillMaxSize(),
    )
}
