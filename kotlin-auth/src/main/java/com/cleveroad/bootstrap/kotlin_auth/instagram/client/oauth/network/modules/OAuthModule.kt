package com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.modules

import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.*
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.api.InstagramAuthApi
import com.cleveroad.bootstrap.kotlin_auth.utils.addParameter
import io.reactivex.rxjava3.core.Single

interface OAuthModule {

    fun getAuthorizationUrl(clientId: String,
                            scopes: String,
                            redirectUrl: String): Single<String>

    fun getAccessToken(code: String,
                       redirectUrl: String,
                       clientId: String,
                       clientSecret: String): Single<String>
}

class OAuthModuleImpl(private val api: InstagramAuthApi) : OAuthModule {

    override fun getAuthorizationUrl(clientId: String, scopes: String, redirectUrl: String): Single<String> =
            Single.just(BASE_AUTH_URL)
                    .map { authUrl ->
                        authUrl.addParameter(PARAM_RESPONSE_TYPE, RESPONSE_TYPE)
                                .addParameter(PARAM_APP_ID, clientId)
                                .addParameter(PARAM_REDIRECT_URL, redirectUrl)
                                .addParameter(PARAM_SCOPE, scopes)
                    }

    override fun getAccessToken(code: String,
                                redirectUrl: String,
                                clientId: String,
                                clientSecret: String) =
            api.getAccessToken(code, redirectUrl, clientId, clientSecret)
                    .map { it.accessToken }
}