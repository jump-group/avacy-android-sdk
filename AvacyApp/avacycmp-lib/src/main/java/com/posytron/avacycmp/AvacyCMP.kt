package com.posytron.avacycmp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.webkit.*
import com.posytron.avacycmp.system.CMPSharedPreferencesWrapper
import com.posytron.avacycmp.utils.JsonUtils
import org.json.JSONObject


object AvacyCMP {
    private var _url: String = ""
    private var _sharedPreferencesWrapper: CMPSharedPreferencesWrapper? = null
    private var _dialog: Dialog? = null
    private var _webView: WebView? = null
    private val _handler = Handler(Looper.getMainLooper())

    @JvmStatic
    fun init(context: Context?, url: String) {
        _sharedPreferencesWrapper = CMPSharedPreferencesWrapper(context!!)
        _url = url
    }

    @JvmStatic
    fun check(context: Context?) {
        check(context, null, null)
    }

    @JvmStatic
    fun check(context: Context?, url: String?) {
        check(context, url, null)
    }

    @JvmStatic
    fun check(context: Context?, listener: OnCMPReady) {
        check(context, "", listener)
    }

    @SuppressLint("SetJavaScriptEnabled")
    @JvmStatic
    internal fun check(context: Context?, url: String?, listener: OnCMPReady?) {
        if (_sharedPreferencesWrapper == null) {
            listener?.onError(context!!.getString(R.string.init_missing))
            return
        }
        var urlToLoad = _url
        if (!url.isNullOrEmpty()) {
            urlToLoad = url
        }
        _dialog = Dialog(context!!, R.style.dialog_style)
        _dialog!!.setContentView(R.layout.cmp_dialog)
        _dialog!!.setCancelable(false);
        val lp = WindowManager.LayoutParams()
        val window = _dialog!!.window
        lp.copyFrom(window!!.attributes)
        lp.width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        window.attributes = lp
        _webView = _dialog!!.findViewById(R.id.web_view)
        _webView!!.settings.domStorageEnabled = true
        _webView!!.settings.javaScriptEnabled = true
        _webView!!.addJavascriptInterface(CMPWebInterface(context), CMPWebInterface.TAG)
        _webView!!.settings.javaScriptCanOpenWindowsAutomatically = true
        _webView!!.webChromeClient = object : WebChromeClient() {}
        _webView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                listener?.onSuccess()
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                listener?.onError(description)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest
            ): Boolean {
                return false
            }
        }
        _webView!!.clearCache(true);
        _webView!!.loadUrl(urlToLoad)
        _dialog!!.setOnShowListener {
            if (_webView!!.contentHeight > (context.resources.displayMetrics.heightPixels * 0.80).toInt()) {
                lp.height = (context.resources.displayMetrics.heightPixels * 0.80).toInt()
            } else {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            window.attributes = lp
        }
        _dialog!!.setOnCancelListener {
            _webView!!.destroy()
            _webView = null
        }
    }

    private fun cancel() {
        if (_dialog != null && _dialog!!.isShowing) {
            _dialog!!.cancel()
        }
    }

    internal fun show() {
        if (_webView != null) {
            _handler.post {
                _dialog!!.show()
            }
        }
    }

    internal fun destroy() {
        _handler.post {
            cancel()
        }
    }

    internal fun read(key: String?): String? {
        return _sharedPreferencesWrapper!!.getString(key)
    }

    internal fun write(key: String?, value: String?): String? {
        return _sharedPreferencesWrapper!!.saveString(key, value)
    }

    @Suppress("UNCHECKED_CAST")
    fun readAll(): String {
        val sharedPreferencesStored = _sharedPreferencesWrapper!!.getAll()
        return JsonUtils.mapToJson(sharedPreferencesStored as MutableMap<String?, Any>).toString()
    }

    @Suppress("UNCHECKED_CAST")
    internal fun writeAll(jsonString: String?): String? {
        val map = JsonUtils.jsonToMap(JSONObject(jsonString!!))
        for ((key, value) in map.entries) {
            _sharedPreferencesWrapper!!.saveString(key, value.toString())
        }
        return JsonUtils.mapToJson(map as MutableMap<String?, Any>).toString()
    }

    internal fun evaluateJavascript(script: String) {
        _handler.post {
            _webView!!.evaluateJavascript(
                "javascript:$script;",
                null
            )
        }
    }

    @JvmStatic
    fun showPreferenceCenter(context: Context?) {
        showPreferenceCenter(context, null)
    }

    @JvmStatic
    fun showPreferenceCenter(context: Context?, listener: OnCMPReady?) {
        if (_sharedPreferencesWrapper == null) {
            listener?.onError(context!!.getString(R.string.init_missing))
            return
        }
        var urlToLoad = _url
        check(context, "$urlToLoad?prefcenter=1", listener)
    }

    abstract class OnCMPReady {
        open fun onSuccess() {}
        abstract fun onError(error: String?)
    }
}