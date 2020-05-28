package com.cleveroad.bootstrap.kotlin_auth.twitter

import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper
import com.cleveroad.bootstrap.kotlin_auth.base.AuthType
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.AuthCallback
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.TwitterAuthClient
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.errors.TwitterAuthException
import java.lang.ref.WeakReference

class TwitterAuthHelper(private val consumerKey: String,
                        private val consumerSecret: String,
                        private val callbackUrl: String,
                        callback: TwitterAuthCallback) : AuthHelper {

    companion object {
        /**
         * Use it if you do not have redirect URL.
         */
        const val NO_CALLBACK = "oob"
    }

    private val callbackRef = WeakReference(callback)

    private var authClient: TwitterAuthClient? = null

    private val authCallback = object : AuthCallback {

        override fun onSuccess(accessToken: String) {
            callbackRef.get()?.onSuccess(AuthType.TWITTER_AUTH, accessToken)
        }

        override fun onFail(exc: TwitterAuthException) {
            callbackRef.get()?.onFail(AuthType.TWITTER_AUTH, exc)
        }

        override fun onCancel() {
            callbackRef.get()?.onCancel()
        }
    }

    override fun auth() {
        authClient?.authorize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        authClient?.onActivityResult(requestCode, resultCode, data)
    }

    override fun connect() {
        callbackRef.get()?.getActivityForResult()?.let { activity ->
            authClient = TwitterAuthClient(consumerKey, consumerSecret, callbackUrl, activity, authCallback)
        }
    }

    override fun disconnect() = Unit
}