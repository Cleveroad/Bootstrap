package com.cleveroad.bootstrap.kotlin_core.ui

import android.view.View
import androidx.annotation.StringRes
import com.cleveroad.bootstrap.kotlin_core.R

interface BaseView : ProgressView {

    /**
     * Show SnackBar with [String]
     *
     * @param rootView Instance of [View]
     * @param text     String text to show
     */
    fun showSnackBar(rootView: View?, text: String?)

    /**
     * Show SnackBar with [StringRes]
     *
     * @param res Id of StringRes text to show
     */
    fun showSnackBar(@StringRes res: Int)

    /**
     * Show SnackBar with [String]
     *
     * @param text String text to show
     */
    fun showSnackBar(text: String?)

    /**
     * Show SnackBar with [StringRes]
     *
     * @param rootView Instance of [View]
     * @param res      Id of StringRes text to show
     */
    fun showSnackBar(rootView: View?, @StringRes res: Int)

    /**
     * Show SnackBar with [StringRes] and function for execution
     *
     * @param actionFun  Function for execution
     * @param text       Id of StringRes text to show
     * @param buttonText Id of StringRes button text  to show
     */
    fun showSnackBar(@StringRes text: Int,
                     @StringRes buttonText: Int = R.string.ok,
                     actionFun: () -> Unit,
                     duration: Int = BASE_SNACK_BAR_DURATION)

    /**
     * Error handler
     *
     * @param error object [Any] which describe error
     */
    fun onError(error: Any)
}