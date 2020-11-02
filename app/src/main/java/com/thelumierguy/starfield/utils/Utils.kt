package com.thelumierguy.starfield.utils

const val ALPHA = 0.05F

fun lowPass(
    input: FloatArray,
    output: FloatArray
) {
    output[0] = ALPHA * input[1] + output[0] * 1.0f - ALPHA
}

sealed class ScreenStates {
    object APP_INIT : ScreenStates()
    object GAME_MENU : ScreenStates()
    object START_GAME : ScreenStates()
}