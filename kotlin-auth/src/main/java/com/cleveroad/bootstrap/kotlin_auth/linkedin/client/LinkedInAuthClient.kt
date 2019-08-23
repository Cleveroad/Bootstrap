package com.cleveroad.bootstrap.kotlin_auth.linkedin.client

import android.app.Activity
import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.errors.LinkedInAuthException
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.OAuthHandler
import com.cleveroad.bootstrap.kotlin_ext.safeLet
import java.lang.ref.WeakReference

class LinkedInAuthClient(private val clientId: String,
                         private val clientSecret: String,
                         private val scopes: List<String>,
                         private val callbackUrl: String,
                         activity: Activity,
                         callback: AuthCallback) {

    companion object {
        private const val SCOPES_SEPARATOR = ","
    }

    private val activityRef = WeakReference(activity)
    private val callbackRef = WeakReference(callback)

    private val authState = LinkedInAuthState()

    fun authorize() {
        if (checkActivityRunning()) handleAuthorize()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        authState.handler?.handleActivityResult(requestCode, resultCode, data)
    }

    private fun handleAuthorize() {
        if (!authUsingOAuth()) dispatchFailEvent("OAuth fail: Cannot open OAuthActivity")
    }

    private fun authUsingOAuth() = safeLet(activityRef.get(), callbackRef.get()) { activity, callback ->
        authState.beginAuthorize(activity, OAuthHandler(clientId, clientSecret, convertScopes(scopes), callbackUrl, callback))
    } ?: false

    private fun checkActivityRunning() = activityRef.get()?.isFinishing?.not()?.also { isRunning ->
        if (!isRunning) dispatchFailEvent("Activity is finished")
    } ?: false

    private fun dispatchFailEvent(errMessage: String) =
            callbackRef.get()?.onFail(LinkedInAuthException(errMessage))

    private fun convertScopes(scopes: List<String>) =
            scopes.joinToString(SCOPES_SEPARATOR)
}