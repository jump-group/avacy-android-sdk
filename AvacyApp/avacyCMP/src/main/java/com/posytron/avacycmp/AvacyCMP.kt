package com.posytron.avacycmp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import com.posytron.avacycmp.system.CMPSharedPreferencesWrapper

object AvacyCMP {
    private var _url: String = ""
    private var _sharedPreferencesWrapper: CMPSharedPreferencesWrapper? = null
    private var _dialog: Dialog? = null
    private var _webView: WebView? = null
    private val _handler = Handler(Looper.getMainLooper())

    fun init(context: Context?, url: String) {
        _sharedPreferencesWrapper = CMPSharedPreferencesWrapper(context!!)
        _url = url
    }

    fun check(context: Context?) {
        check(context, null, null)
    }

    fun check(context: Context?, url: String?) {
        check(context, url, null)
    }

    fun check(context: Context?, listener: OnCMPReady) {
        check(context, "", listener)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun check(context: Context?, url: String?, listener: OnCMPReady?) {
        var urlToLoad = _url
        if (!url.isNullOrEmpty()) {
            urlToLoad = url
        }
        _dialog = Dialog(context!!, R.style.dialog_style)
        _dialog!!.setContentView(R.layout.cmp_dialog)
        val lp = WindowManager.LayoutParams()
        val window = _dialog!!.window
        lp.copyFrom(window!!.attributes)
        lp.width = (context.getResources().getDisplayMetrics().widthPixels * 0.90).toInt()
        lp.height = (context.getResources().getDisplayMetrics().heightPixels * 0.80).toInt()
        window.attributes = lp
        val progressBar = _dialog!!.findViewById<ProgressBar>(R.id.progress_bar)
        _webView = _dialog!!.findViewById<WebView>(R.id.web_view)
        _webView!!.settings.domStorageEnabled = true
        _webView!!.settings.javaScriptEnabled = true
        _webView!!.addJavascriptInterface(CMPWebInterface(context), CMPWebInterface.TAG)
        progressBar.visibility = View.VISIBLE
        _webView!!.settings.javaScriptCanOpenWindowsAutomatically = true
        _webView!!.webChromeClient = object : WebChromeClient() {}
        _webView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?, error: WebResourceError?
            ) {
                listener?.onError(error)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest
            ): Boolean {
                return false
            }
        }
        _webView!!.loadUrl(urlToLoad)
    }

    fun hide() {
        if (_dialog != null && _dialog!!.isShowing) {
            _dialog!!.hide()
        }
    }

    fun show(context: Context?) {
        Toast.makeText(context, "Show request", Toast.LENGTH_SHORT).show()
        _handler.post {
            _dialog!!.show()
        }
    }

    fun destroy(context: Context?) {
        _handler.post {
            hide()
        }
        Toast.makeText(context, "Destroy request", Toast.LENGTH_SHORT).show()
    }

    fun read(context: Context?, key: String?): String? {
        Toast.makeText(context, "Request to read key $key", Toast.LENGTH_SHORT).show()
        return _sharedPreferencesWrapper!!.getString(key)
    }

    fun write(context: Context?, key: String?, value: String?): String? {
        Toast.makeText(context, "Request to write $key=$value", Toast.LENGTH_SHORT).show()
        return _sharedPreferencesWrapper!!.saveString(key, value)
    }

    fun evaluateJavascript(script: String) {
        _handler.post {
            _webView!!.evaluateJavascript(
                "javascript:$script;",
                null
            )
        }
    }

    fun showPreferenceCenter(context: Context?) {
        showPreferenceCenter(context, null, null)
    }

    fun showPreferenceCenter(context: Context?, url: String?) {
        var urlToLoad = _url
        if (!url.isNullOrEmpty()) {
            urlToLoad = url
        }
        showPreferenceCenter(context, "$urlToLoad?prefcenter=1", null);
    }

    fun showPreferenceCenter(context: Context?, url: String?, listener: OnCMPReady?) {
        var urlToLoad = _url
        if (!url.isNullOrEmpty()) {
            urlToLoad = url
        }
        check(context, "$urlToLoad/?prefcenter=1", listener)
        show(context)
    }

    interface OnCMPReady {
        fun onError(error: WebResourceError?)
    }
}
