package com.posytron.avacycmp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast

object AvacyCMP {
    private var _dialog: Dialog? = null
    private var _url: String = ""

    fun inizialize(url: String) {
        _url = url
    }

    fun view(context: Context?) {
        view(context, "", null)
    }

    fun view(context: Context?, url: String?) {
        view(context, url, null)
    }

    fun view(context: Context?, listener: OnCMPReady) {
        view(context, "", listener)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun view(context: Context?, url: String?, listener: OnCMPReady?) {
        var urlToLoad = _url
        if (!url.isNullOrEmpty()) {
            urlToLoad = url
        }
        _dialog = Dialog(context!!)
        _dialog!!.setContentView(R.layout.cmp_dialog)
        val lp = WindowManager.LayoutParams()
        val window = _dialog!!.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp
        val progressBar = _dialog!!.findViewById<ProgressBar>(R.id.progress_bar)
        val webView = _dialog!!.findViewById<WebView>(R.id.web_view)
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(CMPWebInterface(context), CMPWebInterface.TAG)
        progressBar.visibility = View.VISIBLE

        var errorOccurred = false
        webView.webChromeClient = object : WebChromeClient() {}
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                progressBar.visibility = View.GONE
                if (!errorOccurred)
                    _dialog!!.show()
            }

            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?, error: WebResourceError?
            ) {
                errorOccurred = true;
                hide()
                listener?.onError(error)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest
            ): Boolean {
                return false
            }
        }
        webView.loadUrl(urlToLoad)
    }

    fun hide() {
        if (_dialog != null && _dialog!!.isShowing) {
            _dialog!!.hide()
        }
    }

    fun show(context: Context?) {
        Toast.makeText(context, "Show request", Toast.LENGTH_SHORT).show()
    }

    fun destroy(context: Context?) {
        Toast.makeText(context, "Destroy request", Toast.LENGTH_SHORT).show()
    }

    fun read(context: Context?, value: String?) {
        Toast.makeText(context, "Request to read value $value", Toast.LENGTH_SHORT).show()
    }

    fun write(context: Context?, key: String?, value: String?) {
        Toast.makeText(context, "Request to write the value $key=$value", Toast.LENGTH_SHORT)
            .show()
    }
}
