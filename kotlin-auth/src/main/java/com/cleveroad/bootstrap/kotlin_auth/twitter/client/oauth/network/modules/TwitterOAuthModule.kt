package com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.modules

import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.BASE_AUTH_URL
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.PARAM_TOKEN
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.api.TwitterOAuthApi
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.beans.RequestTokenResponse
import com.cleveroad.bootstrap.kotlin_auth.utils.addParameter
import com.cleveroad.bootstrap.kotlin_auth.utils.convertToRequestTokenModel
import com.cleveroad.bootstrap.kotlin_auth.utils.retrieveResponseParam
import io.reactivex.Single

interface TwitterOAuthModule {

    fun getRequestToken(): Single<RequestTokenResponse>

    fun getAuthorizationUrl(requestToken: String): Single<String>

    fun getAccessToken(): Single<String>
}

class TwitterOAuthModuleImpl(private val api: TwitterOAuthApi) : TwitterOAuthModule {

    override fun getRequestToken(): Single<RequestTokenResponse> =
            api.getRequestToken()
                    .map {
                        it.string().convertToRequestTokenModel()
                    }

    override fun getAuthorizationUrl(requestToken: String): Single<String> =
            Single.just(BASE_AUTH_URL)
                    .map { it.addParameter(PARAM_TOKEN, requestToken) }

    override fun getAccessToken(): Single<String> =
            api.getAccessToken()
                    .map { it.string().retrieveResponseParam(PARAM_TOKEN) }
}