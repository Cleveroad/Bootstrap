package com.cleveroad.bootstrap.kotlin_auth.utils

import android.net.Uri
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.PARAM_TOKEN
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.PARAM_TOKEN_SECRET
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.beans.RequestTokenResponse

fun String.convertToRequestTokenModel(): RequestTokenResponse =
        RequestTokenResponse(retrieveResponseParam(PARAM_TOKEN),
                retrieveResponseParam(PARAM_TOKEN_SECRET))

fun String.addParameter(key: String, value: String) =
        Uri.parse(this)
                .buildUpon()
                .appendQueryParameter(key, value)
                .build()
                .toString()

fun String.retrieveResponseParam(key: String) =
        Uri.parse("?$this").getQueryParameter(key) ?: EMPTY_STRING