package com.contestPM.competition

import android.app.Activity
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class RemoteConfigViewModel : ViewModel() {
    var remoteConfig = Firebase.remoteConfig
    var urlLiveData = MutableLiveData<String>()


    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 500
    }

    fun loadRemoteConfig(context: Activity){
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.default_value)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(context) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                } else {}
            }
        var url = remoteConfig[LOADING_PHASE].asString()
        Log.i("RemoteConfig","$url")
        urlLiveData.setValue(url)
    }


    companion object{
        private const val LOADING_PHASE = "web_url"
    }
}