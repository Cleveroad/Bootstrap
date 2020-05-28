package com.cleveroad.bootstrap.kotlin_auth.instagram

import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper
import com.cleveroad.bootstrap.kotlin_auth.base.AuthType
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.AuthCallback
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.InstagramAuthClient
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.errors.InstagramAuthException
import java.lang.ref.WeakReference

class InstagramAuthHelper(private val clientId: String,
                          private val clientSecret: String,
                          private val scopes: List<String>,
                          private val callbackUrl: String,
                          callback: InstagramAuthCallback) : AuthHelper {

    private val callbackRef = WeakReference(callback)

    private var client: InstagramAuthClient? = null

    private var authCallback = object : AuthCallback {

        override fun onSuccess(accessToken: String) {
            callbackRef.get()?.onSuccess(AuthType.INSTAGRAM_AUTH, accessToken)
        }

        override fun onFail(error: InstagramAuthException) {
            callbackRef.get()?.onFail(AuthType.INSTAGRAM_AUTH, error)
        }
    }

    override fun auth() {
        client?.authorize()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        client?.onActivityResult(requestCode, resultCode, data)
    }

    override fun connect() {
        callbackRef.get()?.getActivityForResult()?.let { activity ->
            client = InstagramAuthClient(clientId,
                    clientSecret, scopes, callbackUrl, activity, authCallback)
        }
    }

    override fun disconnect() = Unit
}