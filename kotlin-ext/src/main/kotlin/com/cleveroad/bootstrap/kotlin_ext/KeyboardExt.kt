package com.cleveroad.bootstrap.kotlin_ext

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment as SupportFragment

internal const val NO_FLAGS = 0

/**
 * Extension method to provide show keyboard
 */
fun Context.showKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        toggleSoftInput(InputMethodManager.SHOW_FORCED, NO_FLAGS)
    }
}

/**
 * Extension method to provide show keyboard for Activity androidx
 */
fun SupportFragment.showKeyboard() = activity?.showKeyboard()

/**
 * Extension method to provide hide keyboard for Activity
 */
fun Activity.hideKeyboard(view: View? = null) = (this as Context).hideKeyboard(
        view
                ?: currentFocus
)

/**
 * Extension method to provide hide keyboard for Context
 */
fun Context.hideKeyboard(view: View? = null) = view?.let {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
        hideSoftInputFromWindow(it.windowToken, NO_FLAGS)
    }
}

/**
 * Extension method to provide hide keyboard for SupportFragment
 */
fun SupportFragment.hideKeyboard(view: View? = null) = activity?.hideKeyboard(view)
