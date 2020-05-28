package com.cleveroad.bootstrap.kotlin_auth.twitter.client

import android.app.Activity
import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.ConfigProvider.config
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.data.AuthConfig
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.errors.TwitterAuthException
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.OAuthHandler
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.sso.SSOAuthHandler
import com.cleveroad.bootstrap.kotlin_ext.safeLet
import java.lang.ref.WeakReference

class TwitterAuthClient(consumerKey: String,
                        consumerSecret: String,
                        callbackUrl: String,
                        activity: Activity,
                        callback: AuthCallback) {

    private val activityRef = WeakReference(activity)
    private val callbackRef = WeakReference(callback)

    private val authState = TwitterAuthState()

    init {
        config = AuthConfig(consumerKey, consumerSecret, callbackUrl)
    }

    fun authorize() {
        if (checkActivityRunning()) handleAuthorize()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        authState.handler?.handleActivityResult(requestCode, resultCode, data)
    }

    private fun handleAuthorize() {
        if (!authUsingSSO() && !authUsingOAuth())
            dispatchFailEvent("Cannot start authentication")
    }

    private fun authUsingSSO() = safeLet(activityRef.get(), callbackRef.get()) { activity, callback ->
        authState.beginAuthorize(activity, SSOAuthHandler(callback))
    } ?: false

    private fun authUsingOAuth() = safeLet(activityRef.get(), callbackRef.get()) { activity, callback ->
        authState.beginAuthorize(activity, OAuthHandler(callback))
    } ?: false

    private fun checkActivityRunning() = activityRef.get()?.isFinishing?.not()?.also { isRunning ->
        if (!isRunning) dispatchFailEvent("Activity is finished")
    } ?: false

    private fun dispatchFailEvent(errMessage: String) =
            callbackRef.get()?.onFail(TwitterAuthException(errMessage))
}