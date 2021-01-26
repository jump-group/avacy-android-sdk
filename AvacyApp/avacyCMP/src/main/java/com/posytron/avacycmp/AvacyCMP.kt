package com.posytron.avacycmp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
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
    fun check(context: Context?, url: String?, listener: OnCMPReady?) {
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
                listener!!.onSuccess()
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

    internal fun show(context: Context?) {
        if (_webView != null) {
            Toast.makeText(context, "Show request", Toast.LENGTH_SHORT).show()
            _handler.post {
                _dialog!!.show()
            }
        }
    }

    internal fun destroy(context: Context?) {
        _handler.post {
            cancel()
        }
        Toast.makeText(context, "Destroy request", Toast.LENGTH_SHORT).show()
    }

    internal fun read(context: Context?, key: String?): String? {
        Toast.makeText(context, "Request to read key $key", Toast.LENGTH_SHORT).show()
        return _sharedPreferencesWrapper!!.getString(key)
    }

    internal fun write(context: Context?, key: String?, value: String?): String? {
        Toast.makeText(context, "Request to write $key=$value", Toast.LENGTH_SHORT).show()
        return _sharedPreferencesWrapper!!.saveString(key, value)
    }

    @Suppress("UNCHECKED_CAST")
    fun readAll(context: Context): String {
        Toast.makeText(context, "Request to read all", Toast.LENGTH_SHORT).show()
        val sharedPreferencesStored = _sharedPreferencesWrapper!!.getAll()
        return JsonUtils.mapToJson(sharedPreferencesStored as MutableMap<String?, Any>).toString()
    }

    @Suppress("UNCHECKED_CAST")
    internal fun writeAll(context: Context?, jsonString: String?): String? {
        Toast.makeText(context, "Request to write $jsonString", Toast.LENGTH_SHORT).show()
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
        showPreferenceCenter(context, null, null)
    }

    @JvmStatic
    fun showPreferenceCenter(context: Context?, url: String?) {
        var urlToLoad = _url
        if (!url.isNullOrEmpty()) {
            urlToLoad = url
        }
        showPreferenceCenter(context, "$urlToLoad?prefcenter=1", null);
    }

    @JvmStatic
    fun showPreferenceCenter(context: Context?, baseUrl: String?, listener: OnCMPReady?) {
        if (_sharedPreferencesWrapper == null) {
            listener?.onError(context!!.getString(R.string.init_missing))
            return
        }
        var urlToLoad = _url
        if (!baseUrl.isNullOrEmpty()) {
            urlToLoad = baseUrl
        }
        check(context, "$urlToLoad?prefcenter=1", object : OnCMPReady() {
            override fun onSuccess() {
                show(context)
            }

            override fun onError(error: String?) {
                listener?.onError(error)
            }
        })
    }

    abstract class OnCMPReady {
        open fun onSuccess() {}
        abstract fun onError(error: String?)
    }
}