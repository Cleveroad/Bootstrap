package com.cleveroad.bootstrap.kotlin_auth.linkedin.client

import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.errors.LinkedInAuthException

interface AuthCallback {

    fun onSuccess(accessToken: String)

    fun onFail(error: LinkedInAuthException)
}