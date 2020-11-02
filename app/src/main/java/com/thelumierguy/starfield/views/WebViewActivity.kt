package com.thelumierguy.starfield.views

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.thelumierguy.starfield.R

class WebView:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view)

        var url = intent.getStringExtra("URL")

        val webview = findViewById<WebView>(R.id.main_web_view)
        webview.settings.setAppCacheEnabled(true)
        webview.settings.setAppCachePath(this.getCacheDir().getPath())
        webview.settings.setCacheMode(WebSettings.LOAD_DEFAULT)
        webview.settings.javaScriptEnabled = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true

        var t = this.getCacheDir().getPath()
        var r = webview.settings.cacheMode.toString()
        cookieManager.setAcceptCookie(true)
        webview.loadUrl(url.toString());
        webview.setWebViewClient(object : WebViewClient(){} )
        webview.setWebChromeClient(object:  WebChromeClient() {} )
    }

}