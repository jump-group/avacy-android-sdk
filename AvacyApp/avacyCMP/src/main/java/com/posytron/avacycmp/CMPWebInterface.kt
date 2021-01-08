package com.posytron.avacycmp

import android.content.Context
import android.webkit.JavascriptInterface

class CMPWebInterface(context: Context) {
    private val context: Context? = context

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
    fun read(value: String?) {
        AvacyCMP.read(context, value)
    }

    @JavascriptInterface
    fun write(key: String?, value: String?) {
        AvacyCMP.write(context, key, value)
    }
}