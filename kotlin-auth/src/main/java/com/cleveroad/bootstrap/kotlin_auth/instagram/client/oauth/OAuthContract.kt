package com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth

const val INSTAGRAM_ENDPOINT = "https://api.instagram.com/"

const val BASE_AUTH_URL = "${INSTAGRAM_ENDPOINT}oauth/authorize"

const val PARAM_RESPONSE_TYPE = "response_type"
const val PARAM_APP_ID = "app_id"
const val PARAM_REDIRECT_URL = "redirect_uri"
const val PARAM_SCOPE = "scope"
const val PARAM_AUTH_CODE = "code"

const val CONTENT_TYPE = "application/x-www-form-urlencoded"
const val RESPONSE_TYPE = "code"
const val GRANT_TYPE = "authorization_code"
