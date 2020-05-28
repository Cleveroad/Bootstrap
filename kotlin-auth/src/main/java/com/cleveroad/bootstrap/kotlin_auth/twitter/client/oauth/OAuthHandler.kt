package com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth

import android.app.Activity
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.AuthCallback
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.AuthHandler
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.ConfigProvider.config
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.data.AuthConfig
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.screen.OAuthActivity
import com.cleveroad.bootstrap.kotlin_ext.returnTrue
import com.cleveroad.bootstrap.kotlin_ext.safeLet

class OAuthHandler(callback: AuthCallback) : AuthHandler(callback) {

    override fun authorize(activity: Activity): Boolean =
            OAuthActivity.start(activity).returnTrue()
}