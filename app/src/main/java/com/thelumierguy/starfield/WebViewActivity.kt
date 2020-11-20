package com.contestPM.competition.views

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.contestPM.competition.R
import com.contestPM.competition.utils.UrlBuilder
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.onesignal.OneSignal

class WebViewActivity:AppCompatActivity() {

    private lateinit var webview:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)
        var main_url = intent.getStringExtra("URL")
        webview = findViewById(R.id.main_web_view)
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
        webview.loadUrl(main_url.toString())
        webview.setWebViewClient(object : WebViewClient(){} )
        webview.setWebChromeClient(object:  WebChromeClient() {} )
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
    }

    override fun onBackPressed() {
        when {
            webview?.canGoBack() == true -> webview.goBack()
            else -> super.onBackPressed()
        }
    }
}