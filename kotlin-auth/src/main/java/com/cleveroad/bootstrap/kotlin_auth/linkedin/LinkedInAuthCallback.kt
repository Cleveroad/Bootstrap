package com.cleveroad.bootstrap.kotlin_auth.linkedin

import android.app.Activity
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper

/**
 * Callback interface from [LinkedInAuthHelper]
 */
interface LinkedInAuthCallback : AuthHelper.AuthorizationCallback {

    /**
     * @return Instance of [Activity] for receiving result from LinkedIn Auth activity.
     */
    fun getActivityForResult(): Activity?
}