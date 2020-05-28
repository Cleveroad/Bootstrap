package com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.api

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import retrofit2.http.POST

interface TwitterOAuthApi {

    @POST("/oauth/request_token")
    fun getRequestToken(): Single<ResponseBody>

    @POST("/oauth/access_token")
    fun getAccessToken(): Single<ResponseBody>
}