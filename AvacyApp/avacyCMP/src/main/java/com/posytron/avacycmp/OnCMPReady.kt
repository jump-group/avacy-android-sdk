package com.posytron.avacycmp

import android.webkit.WebResourceError

interface OnCMPReady {
    fun onError(error: WebResourceError?)
}