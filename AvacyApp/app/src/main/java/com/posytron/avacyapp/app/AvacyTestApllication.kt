package com.posytron.avacyapp.app

import android.app.Application
import android.webkit.WebView
import com.posytron.avacycmp.AvacyCMP

class AvacyTestApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
        WebView.setWebContentsDebuggingEnabled(true);
        AvacyCMP.init(
            this,
            //"https://avacy-banner-d24ozpfr4.vercel.app/demos/rai-b.html"
            "https://posytron.com/avacy.html"
        )
    }
}