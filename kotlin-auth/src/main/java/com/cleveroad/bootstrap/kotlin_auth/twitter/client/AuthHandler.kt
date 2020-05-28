package com.cleveroad.bootstrap.kotlin_auth.twitter.client

import android.app.Activity
import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.data.TWITTER_AUTH_REQUEST_CODE
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.errors.TwitterAuthException
import com.cleveroad.bootstrap.kotlin_ext.returnFalse
import com.cleveroad.bootstrap.kotlin_ext.returnTrue
import java.lang.ref.WeakReference

abstract class AuthHandler(callback: AuthCallback) {

    companion object {
        internal const val EXTRA_TOKEN = "tk"
        internal const val EXTRA_AUTH_ERROR = "auth_error"
    }

    private val callbackRef = WeakReference(callback)

    abstract fun authorize(activity: Activity): Boolean

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = run {
        if (requestCode != TWITTER_AUTH_REQUEST_CODE) return false
        when (resultCode) {
            Activity.RESULT_OK -> dispatchSuccessEvent(data).returnTrue()
            else -> dispatchFailEvent(data).returnFalse()
        }
    }

    private fun dispatchSuccessEvent(data: Intent?) = data?.run {
        callbackRef.get()?.onSuccess(getStringExtra(EXTRA_TOKEN))
    }

    private fun dispatchFailEvent(data: Intent?) = data?.run {
        callbackRef.get()?.onFail(TwitterAuthException(getStringExtra(EXTRA_AUTH_ERROR)))
    }
}