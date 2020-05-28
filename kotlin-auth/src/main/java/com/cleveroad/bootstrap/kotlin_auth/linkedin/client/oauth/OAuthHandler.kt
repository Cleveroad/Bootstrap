package com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth

import android.app.Activity
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.AuthCallback
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.AuthHandler
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.screen.OAuthActivity

class OAuthHandler(clientId: String,
                   clientSecret: String,
                   scopes: String,
                   callbackUrl: String,
                   callback: AuthCallback) : AuthHandler(clientId, clientSecret, scopes, callbackUrl, callback) {

    override fun authorize(activity: Activity): Boolean =
            OAuthActivity.start(activity, clientId, clientSecret, scopes, callbackUrl)
}