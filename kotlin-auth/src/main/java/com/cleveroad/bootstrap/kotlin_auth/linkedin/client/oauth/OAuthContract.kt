package com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth

const val LINKEDIN_ENDPOINT = "https://www.linkedin.com/"

const val BASE_AUTH_URL = "${LINKEDIN_ENDPOINT}oauth/v2/authorization"

const val PARAM_RESPONSE_TYPE = "response_type"
const val PARAM_CLIENT_ID = "client_id"
const val PARAM_REDIRECT_URL = "redirect_uri"
const val PARAM_STATE = "state"
const val PARAM_SCOPE = "scope"
const val PARAM_AUTH_CODE = "code"

const val RESPONSE_TYPE = "code"

const val GRANT_TYPE = "authorization_code"

const val CONTENT_TYPE = "application/x-www-form-urlencoded"