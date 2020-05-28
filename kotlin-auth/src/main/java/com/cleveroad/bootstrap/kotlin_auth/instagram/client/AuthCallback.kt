package com.cleveroad.bootstrap.kotlin_auth.instagram.client

import com.cleveroad.bootstrap.kotlin_auth.instagram.client.errors.InstagramAuthException


interface AuthCallback {

    fun onSuccess(accessToken: String)

    fun onFail(error: InstagramAuthException)
}