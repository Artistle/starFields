package com.thelumierguy.starfield

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData


class DeepLinkViewModel:ViewModel() {
    val liveDataDeepLink = MutableLiveData<String>()

    fun requestDeepLink(context: Activity){

    }
}