package com.cleveroad.bootstrap.kotlin_auth.linkedin

import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper
import com.cleveroad.bootstrap.kotlin_auth.base.AuthType
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.AuthCallback
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.LinkedInAuthClient
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.errors.LinkedInAuthException
import java.lang.ref.WeakReference

class LinkedInAuthHelper(private val clientId: String,
                         private val clientSecret: String,
                         private val scopes: List<String>,
                         private val callbackUrl: String,
                         callback: LinkedInAuthCallback) : AuthHelper {

    private val callbackRef = WeakReference(callback)

    private var client: LinkedInAuthClient? = null

    private var authCallback = object : AuthCallback {

        override fun onSuccess(accessToken: String) {
            callbackRef.get()?.onSuccess(AuthType.LINKEDIN_AUTH, accessToken)
        }

        override fun onFail(error: LinkedInAuthException) {
            callbackRef.get()?.onFail(AuthType.LINKEDIN_AUTH, error)
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
            client = LinkedInAuthClient(clientId,
                    clientSecret, scopes, callbackUrl, activity, authCallback)
        }
    }

    override fun disconnect() = Unit
}