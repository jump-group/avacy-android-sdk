package com.posytron.avacycmp

import android.content.Context
import android.webkit.JavascriptInterface

internal class CMPWebInterface(private val context: Context) {

    companion object {
        var TAG: String = "CMPWebInterface"
    }

    @JavascriptInterface
    fun show() {
        AvacyCMP.show()
    }

    @JavascriptInterface
    fun destroy() {
        AvacyCMP.destroy()
    }

    @JavascriptInterface
    fun read(key: String?) {
        AvacyCMP.read(key)
    }

    @JavascriptInterface
    fun read(key: String?, callback: String?) {
        val result = AvacyCMP.read(key)
        AvacyCMP.evaluateJavascript("$callback('$result');")
    }

    @JavascriptInterface
    fun write(key: String?, value: String?) {
        AvacyCMP.write(key, value)
    }

    @JavascriptInterface
    fun write(key: String?, value: String?, callback: String?) {
        val result = AvacyCMP.write(key, value)
        AvacyCMP.evaluateJavascript("$callback('$result');")
    }

    @JavascriptInterface
    fun readAll() {
        AvacyCMP.readAll()
    }

    @JavascriptInterface
    fun readAll(callback: String?) {
        val result = AvacyCMP.readAll()
        AvacyCMP.evaluateJavascript("$callback('$result');")
    }

    @JavascriptInterface
    fun writeAll(jsonString: String?) {
        AvacyCMP.writeAll(jsonString)
    }

    @JavascriptInterface
    fun writeAll(jsonString: String?, callback: String?) {
        val result = AvacyCMP.writeAll(jsonString)
        AvacyCMP.evaluateJavascript("$callback('$result');")
    }
}