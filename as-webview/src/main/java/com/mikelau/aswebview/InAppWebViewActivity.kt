package com.mikelau.aswebview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mikelau.aswebview.utils.ValidateTokenUtil
import com.mikelau.aswebview.viewmodels.InAppWebViewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


const val IN_APP_WEB_VIEW_TAG = "webViewTag"
const val IN_APP_WEB_VIEW_TITLE = "title"
const val IN_APP_WEB_VIEW_URL = "url"
const val IN_APP_WEB_VIEW_JWT = "jwt"
const val IN_APP_WEB_VIEW_IS_EXTERNAL = "isExternal"
const val IN_APP_WEB_VIEW_IS_AUTHENTICATED = "isAuthenticated"

class InAppWebViewActivity : ComponentActivity() {

    private val inAppWebViewViewModel: InAppWebViewViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = ColorPrimary.toArgb()

        ValidateTokenUtil.initialize(this)

        val title = intent.getStringExtra(IN_APP_WEB_VIEW_TITLE) ?: ""
        val jwt = intent.getStringExtra(IN_APP_WEB_VIEW_JWT) ?: ""
        initialUrl = intent.getStringExtra(IN_APP_WEB_VIEW_URL) ?: ""
        val isExternal = intent.getBooleanExtra(IN_APP_WEB_VIEW_IS_EXTERNAL, false)
        val isAuthenticated = intent.getBooleanExtra(IN_APP_WEB_VIEW_IS_AUTHENTICATED, false)

        inAppWebViewViewModel.initAsWebView(isAuthenticated = isAuthenticated, jwt = jwt)

        setContent {
            val showError by inAppWebViewViewModel.showError
            val loading = inAppWebViewViewModel.isLoading.collectAsState()

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
            ) {
                loading.value.let {
                    if (it) {
                        LoadingView()
                    } else {
                        Column {
                            ToolbarView(
                                title = title,
                                onBackPressedDispatcher = onBackPressedDispatcher
                            )
                            if (showError) {
                                InAppWebViewErrorView()
                            } else {
                                InAppWebView(initialUrl, isExternal)
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ToolbarView(title: String, onBackPressedDispatcher: OnBackPressedDispatcher) {
        TopAppBar(
            colors = TopAppBarColors(
                containerColor = ColorPrimary,
                scrolledContainerColor = Color.White,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
            ),
            title = {
                Text(text = title, color = Color.White)
            },
            navigationIcon = {
                IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }

    @Composable
    fun LoadingView() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun InAppWebViewErrorView() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "An error occured.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 24.dp
                )
            )
            Text(
                text = "There was an error, please try again later.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 24.dp
                )
            )
            Button(
                modifier = Modifier.padding(all = 12.dp), onClick = {
                    inAppWebViewViewModel.setErrorState(false)
                }) {
                Text(text = "Retry")
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun InAppWebView(loadUrl: String, isExternal: Boolean) {
        AndroidView(
            modifier = Modifier.testTag(IN_APP_WEB_VIEW_TAG),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    clearCache(true)

                    // Set user agent to setup ASCom customization (no header/footer, use excursion background and etc.)
                    val userAgentString: String = settings.userAgentString
                    settings.userAgentString = String.format(
                        "%s %s",
                        userAgentString,
                        "ALKApp/Android NativeHeader"
                    )

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowContentAccess = false
                    settings.allowFileAccess = false
                    webViewClient = object : WebViewClient() {
                        @SuppressLint("WebViewClientOnReceivedSslError")
                        override fun onReceivedSslError(
                            view: WebView?,
                            handler: SslErrorHandler,
                            error: SslError?
                        ) {
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            if (isExternal) {
                                request?.url?.let {
                                    val intent = Intent(Intent.ACTION_VIEW, it)
                                    context.startActivity(intent)
                                    return true
                                }
                            }
                            return false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            errorCode: Int,
                            description: String?,
                            failingUrl: String?
                        ) {
                            inAppWebViewViewModel.setErrorState(true)
                        }
                    }
                    loadUrl(loadUrl)
                }
            }, update = {
                it.loadUrl(loadUrl)
            })
    }

    companion object {
        var initialUrl = ""
    }
}