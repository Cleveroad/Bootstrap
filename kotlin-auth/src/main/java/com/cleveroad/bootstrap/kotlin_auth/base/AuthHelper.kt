package com.cleveroad.bootstrap.kotlin_auth.base

import android.app.Activity
import android.content.Intent
import androidx.annotation.IntDef
import androidx.fragment.app.Fragment

/**
 * Interface for authorization with social Networks
 */
interface AuthHelper {

    /**
     * Starts authorization flow
     */
    fun auth()

    /**
     * Delivers result from [Activity.onActivityResult]
     * or [Fragment.onActivityResult]
     * to Authorization SDK realization
     *
     * @param requestCode The integer request code originally supplied to
     * startActivityForResult(), allowing you to identify who this
     * result came from.
     * @param resultCode  The integer result code returned by the child activity
     * through its setResult().
     * @param data        An Intent, which can return result data to the caller
     * (various data can be attached to Intent "extras").
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    /**
     * Must be called in [Activity.onResume] or [Fragment.onResume]
     */
    fun connect()

    /**
     * Must be called in [Activity.onPause] or [Fragment.onPause]
     */
    fun disconnect()

    /**
     * Callback interface from helper
     */
    interface AuthorizationCallback {

        /**
         * Method called after successful authorization
         *
         * @param authType Type of social Network. Must be one
         * of [AuthHelper.AuthType]
         * @param token    Authorization token from network
         */
        fun onSuccess(authType: AuthType, token: String)

        /**
         * Called after failed authorization
         *
         * @param authType  - The Authorization token from network
         * @param throwable - The throwable from social authorization SDK
         */
        fun onFail(authType: AuthType, throwable: Throwable?)
    }
}
