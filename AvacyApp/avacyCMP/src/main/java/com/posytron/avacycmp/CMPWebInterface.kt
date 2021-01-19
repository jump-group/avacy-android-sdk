package com.posytron.avacycmp

import android.content.Context
import android.webkit.JavascriptInterface

internal class CMPWebInterface(private val context: Context) {

    companion object {
        var TAG: String = "CMPWebInterface"
    }

    @JavascriptInterface
    fun show() {
        AvacyCMP.show(context)
    }

    @JavascriptInterface
    fun destroy() {
        AvacyCMP.destroy(context)
    }

    @JavascriptInterface
    fun read(key: String?) {
        AvacyCMP.read(context, key)
    }

    @JavascriptInterface
    fun read(key: String?, callback: String?) {
        val result = AvacyCMP.read(context, key)
        AvacyCMP.evaluateJavascript("$callback('$result');")
    }

    @JavascriptInterface
    fun write(key: String?, value: String?) {
        AvacyCMP.write(context, key, value)
    }

    @JavascriptInterface
    fun write(key: String?, value: String?, callback: String?) {
        val result = AvacyCMP.write(context, key, value)
        AvacyCMP.evaluateJavascript("$callback('$result');")
    }
}