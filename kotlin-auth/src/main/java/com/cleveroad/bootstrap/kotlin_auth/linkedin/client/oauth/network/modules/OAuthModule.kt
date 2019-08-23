package com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.network.modules

import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.*
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.network.api.LinkedInAuthApi
import com.cleveroad.bootstrap.kotlin_auth.utils.addParameter
import io.reactivex.Single
import java.util.*

interface OAuthModule {

    fun getAuthorizationUrl(clientId: String,
                            scopes: String,
                            redirectUrl: String): Single<String>

    fun getAccessToken(code: String,
                       redirectUrl: String,
                       clientId: String,
                       clientSecret: String): Single<String>
}

class OAuthModuleImpl(private val api: LinkedInAuthApi) : OAuthModule {

    override fun getAuthorizationUrl(clientId: String, scopes: String, redirectUrl: String): Single<String> =
            Single.just(BASE_AUTH_URL)
                    .map { authUrl ->
                        authUrl.addParameter(PARAM_RESPONSE_TYPE, RESPONSE_TYPE)
                                .addParameter(PARAM_CLIENT_ID, clientId)
                                .addParameter(PARAM_REDIRECT_URL, redirectUrl)
                                .addParameter(PARAM_STATE, UUID.randomUUID().toString())
                                .addParameter(PARAM_SCOPE, scopes)
                    }

    override fun getAccessToken(code: String,
                                redirectUrl: String,
                                clientId: String,
                                clientSecret: String) =
            api.getAccessToken(code, redirectUrl, clientId, clientSecret)
                    .map { it.accessToken }
}