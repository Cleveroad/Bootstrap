package com.cleveroad.bootstrap.kotlin_auth.linkedin.client

import android.app.Activity
import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.data.LINKEDIN_AUTH_REQUEST_CODE
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.errors.LinkedInAuthException
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils.Companion.getExtra
import com.cleveroad.bootstrap.kotlin_ext.returnTrue
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
        if (requestCode != LINKEDIN_AUTH_REQUEST_CODE) return false
        when (resultCode) {
            Activity.RESULT_OK -> dispatchSuccessEvent(data).returnTrue()
            Activity.RESULT_CANCELED -> dispatchFailEvent(data).returnTrue()
            else -> false
        }
    }

    private fun dispatchSuccessEvent(data: Intent?) = data?.run {
        callbackRef.get()?.onSuccess(getStringExtra(EXTRA_ACCESS_TOKEN))
    }

    private fun dispatchFailEvent(data: Intent?) = data?.run {
        callbackRef.get()?.onFail(LinkedInAuthException(getStringExtra(EXTRA_AUTH_ERROR)))
    }
}