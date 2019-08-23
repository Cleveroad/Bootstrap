package com.cleveroad.bootstrap.kotlin_auth.google

import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper

/**
 * Callback interface from [GoogleAuthHelper]
 */
interface GoogleAuthCallback : AuthHelper.AuthorizationCallback {

    /**
     * Implementation must call [Activity.startActivityForResult]
     * or [Fragment.startActivityForResult]
     * this method has usage in [GoogleAuthHelper.auth]. So if there are
     * no intents for using Google authorization implementation of this method can be empty
     *
     * @param intent      The intent to start.
     * @param requestCode If >= 0, this code will be returned in
     * onActivityResult() when the activity exits.
     */
    fun startActivityForResult(intent: Intent, requestCode: Int)
}