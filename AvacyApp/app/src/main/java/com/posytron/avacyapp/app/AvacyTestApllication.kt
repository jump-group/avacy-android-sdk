package com.posytron.avacyapp

import android.app.Application
import android.webkit.WebView
import com.posytron.avacycmp.AvacyCMP

class AvacyTestApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
        WebView.setWebContentsDebuggingEnabled(true);
        AvacyCMP.init(
            this,
            "https://www.posytron.com/avacy.html"
        )
    }
}