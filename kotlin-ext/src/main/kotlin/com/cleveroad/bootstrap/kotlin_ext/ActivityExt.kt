package com.cleveroad.bootstrap.kotlin_ext

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.WindowManager

/**
 * To set windowSoftInputMode SOFT_INPUT_ADJUST_NOTHING at runtime
 */
fun Activity.setAdjustNothing() = window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

/**
 * To set windowSoftInputMode SOFT_INPUT_ADJUST_RESIZE at runtime
 */
fun Activity.setAdjustResize() = window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

/**
 * To set windowSoftInputMode SOFT_INPUT_ADJUST_PAN at runtime
 */
fun Activity.setAdjustPan() = window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

/**
 * To hide all screen decorations (such as the status bar) while this window is displayed
 */
fun Activity.setFullScreen() = window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)

/**
 * To set requestedOrientation SCREEN_ORIENTATION_SENSOR at runtime
 */
fun Activity.setRotatable() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
}

/**
 * To set requestedOrientation SCREEN_ORIENTATION_PORTRAIT at runtime
 */
fun Activity.setOnlyPortraitMode() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

/**
 * To set requestedOrientation SCREEN_ORIENTATION_LANDSCAPE at runtime
 */
fun Activity.setOnlyLandscapeMode() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}
