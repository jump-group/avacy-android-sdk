package com.posytron.avacyapp.app

import android.app.Application
import android.webkit.WebView
import com.posytron.avacyapp.R
import com.posytron.avacycmp.AvacyCMP

class AvacyTestApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
        WebView.setWebContentsDebuggingEnabled(true);
        AvacyCMP.init(
            this,
            getString(R.string.base_url)
        )
    }
}