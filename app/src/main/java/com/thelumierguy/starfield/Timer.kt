package com.thelumierguy.starfield

import android.os.Handler

class Timer(runnable: Runnable, interval: Int, started: Boolean) {
    private val runnable: Runnable
    val handler: Handler
    private var paused = false
    var interval: Int
    init {
        handler = Handler()
        this.runnable = runnable
        this.interval = interval
        if (started) startTimer()
    }
    private val task: Runnable = object : Runnable {
        override fun run() {
            if (!paused) {
                runnable.run()
                handler.postDelayed(this, interval.toLong())
            }
        }
    }
    fun startTimer() {
        paused = false
        handler.postDelayed(task, interval.toLong())
    }
    fun stopTimer() {
        paused = true
    }
}