package com.cleveroad.bootstrap.kotlin_auth.instagram.client

import android.app.Activity
import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.data.INSTAGRAM_AUTH_REQUEST_CODE
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.errors.InstagramAuthException
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils.Companion.getExtra
import java.lang.ref.WeakReference

abstract class AuthHandler(protected val clientId: String,
                           protected val clientSecret: String,
                           protected val scopes: String,
                           protected val callbackUrl: String,
                           callback: AuthCallback) {

    companion object {
        val EXTRA_ACCESS_TOKEN = getExtra<String>("ACCESS_TOKEN")

        val EXTRA_AUTH_ERROR = getExtra<String>("AUTH_ERROR")
    }

    private val callbackRef = WeakReference(callback)

    abstract fun authorize(activity: Activity): Boolean

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean = run {
        (requestCode == INSTAGRAM_AUTH_REQUEST_CODE).apply {
            when (resultCode) {
                Activity.RESULT_OK -> dispatchSuccessEvent(data)
                Activity.RESULT_CANCELED -> dispatchFailEvent(data)
            }
        }
    }

    private fun dispatchSuccessEvent(data: Intent?) = data?.run {
        callbackRef.get()?.onSuccess(getStringExtra(EXTRA_ACCESS_TOKEN))
    }

    private fun dispatchFailEvent(data: Intent?) = data?.run {
        callbackRef.get()?.onFail(InstagramAuthException(getStringExtra(EXTRA_AUTH_ERROR)))
    }
}