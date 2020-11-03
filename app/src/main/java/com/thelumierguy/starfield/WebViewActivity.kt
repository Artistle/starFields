package com.contestPM.competition.views

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.contestPM.competition.R
import com.contestPM.competition.RemoteConfigViewModel
import com.thelumierguy.starfield.DeepLinkViewModel

class WebViewActivity:AppCompatActivity() {


    private lateinit var view:WebView

    val remoteConfigViewModel by lazy { ViewModelProvider(this)[DeepLinkViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        remoteConfigViewModel.requestDeepLink(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)

        var t = remoteConfigViewModel.liveDataDeepLink.value
        var url = intent.getStringExtra("URL")

        val webview: WebView = findViewById<WebView>(R.id.main_web_view)
        webview.settings.setAppCacheEnabled(true)
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true

        webview.loadUrl(url.toString());
        webview.setWebViewClient(object : WebViewClient(){} )
        webview.setWebChromeClient(object:  WebChromeClient() {} )
    }
    override fun onBackPressed() {
        when {
            view?.canGoBack() == true -> view.goBack()
            else -> super.onBackPressed()
        }
    }
}