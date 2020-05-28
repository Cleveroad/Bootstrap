package com.cleveroad.bootstrap.kotlin_auth.instagram

import android.app.Activity
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper

/**
 * Callback interface from [InstagramAuthHelper]
 */
interface InstagramAuthCallback : AuthHelper.AuthorizationCallback {

    /**
     * @return Instance of [Activity] for receiving result from Instagram Auth activity.
     */
    fun getActivityForResult(): Activity?
}