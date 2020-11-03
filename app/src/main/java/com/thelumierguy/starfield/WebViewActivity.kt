package com.contestPM.competition.views

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.contestPM.competition.R
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.onesignal.OneSignal

class WebViewActivity:AppCompatActivity() {

    private lateinit var view:WebView
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()

        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(this) { appLinkData ->
            Log.i("Deep","${appLinkData?.targetUri}")
            Log.i("Deep","${appLinkData?.targetUri}")
        }

        var url = intent.getStringExtra("URL")
        val webview: WebView = findViewById<WebView>(R.id.main_web_view)
        webview.settings.setAppCacheEnabled(true)
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true

        webview.loadUrl(decoderUrl(url.toString()));
        webview.setWebViewClient(object : WebViewClient(){} )
        webview.setWebChromeClient(object:  WebChromeClient() {} )
    }
    private fun decoderUrl(coderUrl:String):String{
        var byteUrl = Base64.decode(coderUrl,1)
        var mainUrl = String(byteUrl)
        return mainUrl
    }
    override fun onBackPressed() {
        when {
            view?.canGoBack() == true -> view.goBack()
            else -> super.onBackPressed()
        }
    }
}