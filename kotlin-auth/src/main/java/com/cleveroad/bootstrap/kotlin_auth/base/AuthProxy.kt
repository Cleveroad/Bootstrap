package com.cleveroad.bootstrap.kotlin_auth.base

import android.content.Context
import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.facebook.FacebookAuthCallback
import com.cleveroad.bootstrap.kotlin_auth.facebook.FacebookAuthHelper
import com.cleveroad.bootstrap.kotlin_auth.facebook.FacebookPermission
import com.cleveroad.bootstrap.kotlin_auth.google.GoogleAuthCallback
import com.cleveroad.bootstrap.kotlin_auth.google.GoogleAuthHelper
import com.cleveroad.bootstrap.kotlin_auth.linkedin.LinkedInAuthCallback
import com.cleveroad.bootstrap.kotlin_auth.linkedin.LinkedInAuthHelper
import com.cleveroad.bootstrap.kotlin_auth.twitter.TwitterAuthCallback
import com.cleveroad.bootstrap.kotlin_auth.twitter.TwitterAuthHelper

/**
 * Wrapper class for list of [AuthHelper].
 *
 * Use it when you have a few different [AuthHelper]'s
 */
class AuthProxy {

    private val authHelpers = hashMapOf<AuthType, AuthHelper>()

    private var socialAuthType: AuthType? = null

    fun registerGoogleAuthHelper(serverClientId: String,
                                 context: Context,
                                 scopes: List<String>,
                                 callback: GoogleAuthCallback) {
        authHelpers[AuthType.GOOGLE_PLUS_AUTH] =
                GoogleAuthHelper(serverClientId, context, scopes, callback)
    }

    fun registerFacebookAuthHelper(permissions: List<FacebookPermission>,
                                   callback: FacebookAuthCallback) {
        authHelpers[AuthType.FACEBOOK_AUTH] = FacebookAuthHelper(callback, permissions)
    }

    fun registerTwitterAuthHelper(consumerKey: String,
                                  consumerSecret: String,
                                  callbackUrl: String,
                                  callback: TwitterAuthCallback) {
        authHelpers[AuthType.TWITTER_AUTH] =
                TwitterAuthHelper(consumerKey, consumerSecret, callbackUrl, callback)
    }

    fun registerLinkedInAuthHelper(clientId: String,
                                   clientSecret: String,
                                   scopes: List<String>,
                                   callbackUrl: String,
                                   callback: LinkedInAuthCallback) {
        authHelpers[AuthType.LINKEDIN_AUTH] =
                LinkedInAuthHelper(clientId, clientSecret, scopes, callbackUrl, callback)
    }

    /**
     * Connect all [AuthHelper]'s
     */
    fun connect() {
        authHelpers.values.forEach { it.connect() }
    }

    /**
     * Disconnect all [AuthHelper]'s
     */
    fun disconnect() {
        authHelpers.values.forEach { it.disconnect() }
    }

    /**
     * Auth with service, that related to [type]
     *
     * @param type auth type
     * @throws IllegalArgumentException if no helper with this type is registered
     */
    fun auth(type: AuthType) {
        authHelpers[type]?.auth()?.also {
            socialAuthType = type
        } ?: throw IllegalArgumentException("There is no helper with this auth type")
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        socialAuthType?.let {
            authHelpers[it]?.onActivityResult(requestCode, resultCode, data)
        }
        socialAuthType = null
    }
}