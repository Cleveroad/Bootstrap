package com.cleveroad.bootstrap.kotlin_ext

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Method to translate dp to pixels
 * @return Float calculated density independent pixels (DiP, DP) to device pixels
 *
 * @param dp density independent pixels (DiP, DP)
 */
fun Context.dpToPx(dp: Float) = resources.displayMetrics.density * dp


/**
 * Method to translate pixels to dp
 * @return Float calculated device pixels as density independent pixels (DiP, DP)
 *
 * @param px device pixels
 */
fun Context.pxToDp(px: Float) = px / resources.displayMetrics.density


/**
 * @return Returns size of screen as Point
 */
fun Context.getScreenSize() = Point().apply {
    (getSystemService(Context.WINDOW_SERVICE) as? WindowManager)
            ?.defaultDisplay?.getSize(this)
}

/**
 * @return Returns size of screen as DisplayMetrics
 */
fun Context.getScreenDisplayMetrics() = DisplayMetrics().apply {
    (getSystemService(Context.WINDOW_SERVICE) as? WindowManager)
            ?.defaultDisplay?.getMetrics(this)
}