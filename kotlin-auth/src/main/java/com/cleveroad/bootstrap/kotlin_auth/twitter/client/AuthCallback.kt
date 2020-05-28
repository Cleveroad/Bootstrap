package com.cleveroad.bootstrap.kotlin_auth.twitter.client

import com.cleveroad.bootstrap.kotlin_auth.twitter.client.errors.TwitterAuthException

interface AuthCallback {

    fun onSuccess(accessToken: String)

    fun onFail(exc: TwitterAuthException)

    fun onCancel()
}