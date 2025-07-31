package bluebirdstudio.app.securenotespinlock.ui.components

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RichTextEditor(
    initialContent: String = "",
    onContentChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    onWebViewCreated: ((WebView) -> Unit)? = null
) {
    val context = LocalContext.current
    var webView: WebView? by remember { mutableStateOf(null) }
    var isWebViewReady by remember { mutableStateOf(false) }

    val jsInterface = remember {
        object {
            @JavascriptInterface
            fun onContentChanged(content: String) {
                try {
                    val jsonObject = JSONObject(content)
                    val htmlContent = jsonObject.getString("html")
                    onContentChanged(htmlContent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        isWebViewReady = true
                        if (initialContent.isNotEmpty()) {
                            evaluateJavascript(
                                "setContent('${initialContent.replace("'", "\\'")}')",
                                null
                            )
                        }
                    }
                }

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                }

                addJavascriptInterface(jsInterface, "Android")
                loadUrl("file:///android_asset/quill-editor.html")

                webView = this
                onWebViewCreated?.invoke(this) // پاس دادن WebView به بیرون
            }
        }
    )

    LaunchedEffect(isWebViewReady) {
        if (isWebViewReady && initialContent.isNotEmpty()) {
            delay(300)
            webView?.evaluateJavascript(
                "setContent('${initialContent.replace("'", "\\'")}')",
                null
            )
        }
    }
}
