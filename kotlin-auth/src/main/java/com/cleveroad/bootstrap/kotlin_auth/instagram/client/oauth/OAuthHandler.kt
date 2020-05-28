package com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth

import android.app.Activity
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.AuthCallback
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.AuthHandler
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.screen.OAuthActivity

class OAuthHandler(clientId: String,
                   clientSecret: String,
                   scopes: String,
                   callbackUrl: String,
                   callback: AuthCallback) : AuthHandler(clientId, clientSecret, scopes, callbackUrl, callback) {

    override fun authorize(activity: Activity): Boolean =
            OAuthActivity.start(activity, clientId, clientSecret, scopes, callbackUrl)
}