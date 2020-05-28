package com.cleveroad.bootstrap.kotlin_ext

import android.os.SystemClock
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewTreeObserver

private const val ALPHA_ENABLED = 1.0f
private const val ALPHA_DISABLED = 0.5f
private const val DEFAULT_CLICK_DEBOUNCE_TIME = 500L

/**
 * Set click listener to views
 *
 * @param views
 */
fun OnClickListener.setClickListeners(vararg views: View) {
    views.forEach { view -> view.setOnClickListener(this) }
}

fun OnClickListener.setClickListenerWithDebounce(vararg views: View, debounceTime: Long = DEFAULT_CLICK_DEBOUNCE_TIME) {
    val clickListener = object : OnClickListener {
        private var lastClickTime = 0L
        override fun onClick(v: View) {
            SystemClock.elapsedRealtime()
                    .takeIf { it - lastClickTime > debounceTime }
                    ?.run {
                        this@setClickListenerWithDebounce.onClick(v)
                        lastClickTime = this
                    }
        }
    }
    views.forEach { view -> view.setOnClickListener(clickListener) }
}

fun View.clickWithDebounce(debounceTime: Long = DEFAULT_CLICK_DEBOUNCE_TIME, action: () -> Unit) {
    setOnClickListener(object : OnClickListener {
        private var lastClickTime = 0L
        override fun onClick(v: View) {
            SystemClock.elapsedRealtime().takeIf { it - lastClickTime > debounceTime }
                    ?.run {
                        action()
                        lastClickTime = this
                    }
        }
    })
}

fun View.OnFocusChangeListener.setFocusChangedListeners(vararg views: View) {
    views.forEach { view -> view.onFocusChangeListener = this }
}

fun removeFocusChangedListeners(vararg views: View?) {
    views.forEach { view -> view?.onFocusChangeListener = null }
}

/**
 * @return true if view is visible
 */
fun View.isVisible() = visibility == VISIBLE

/**
 * If gone set visibility GONE else INVISIBLE
 */
fun View.hide(gone: Boolean = true) {
    visibility = if (gone) GONE else INVISIBLE
}

/**
 * Set view visibility VISIBLE
 */
fun View.show() {
    visibility = VISIBLE
}

fun View?.setVisibility(isVisible: Boolean, gone: Boolean = true) = this?.let { if (isVisible) show() else hide(gone) }

fun View?.setEnabledWithAlpha(isEnable: Boolean, disableAlpha: Float = ALPHA_DISABLED, enabledAlpha: Float = ALPHA_ENABLED) {
    this?.apply {
        isEnabled = isEnable
        alpha = if (isEnable) enabledAlpha else disableAlpha
    }
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun View.applyLayoutParams(block: ViewGroup.MarginLayoutParams.() -> Unit) =
        (layoutParams as? ViewGroup.MarginLayoutParams)
                ?.let { layoutParams = it.apply { block(this) } }