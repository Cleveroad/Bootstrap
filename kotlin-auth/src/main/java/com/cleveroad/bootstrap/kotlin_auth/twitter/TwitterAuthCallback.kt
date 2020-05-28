package com.cleveroad.bootstrap.kotlin_auth.twitter

import android.app.Activity
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper

/**
 * Callback interface from [TwitterAuthHelper]
 */
interface TwitterAuthCallback : AuthHelper.AuthorizationCallback {

    /**
     * @return Instance of [Activity] for receiving result from Twitter Auth activity.
     */
    fun getActivityForResult(): Activity?

    /**
     * Called when user cancels authorization
     */
    fun onCancel()
}