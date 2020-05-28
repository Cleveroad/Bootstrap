package com.cleveroad.bootstrap.kotlin_auth.facebook

import android.app.Activity
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper

/**
 * Callback interface from [FacebookAuthHelper]
 */
interface FacebookAuthCallback : AuthHelper.AuthorizationCallback {

    /**
     * @return Instance of [Activity] for receiving result from [FacebookActivity].
     */
    fun getActivityForResult(): Activity?

    /**
     * Calls if user canceled authorization
     */
    fun onCancel()
}