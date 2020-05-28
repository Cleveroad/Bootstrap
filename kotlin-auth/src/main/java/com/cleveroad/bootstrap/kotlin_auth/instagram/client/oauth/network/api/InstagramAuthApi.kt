package com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.api

import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.CONTENT_TYPE
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.GRANT_TYPE
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.beans.AccessTokenBean
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface InstagramAuthApi {

    @FormUrlEncoded
    @POST("oauth/access_token")
    fun getAccessToken(@Field("code") code: String,
                       @Field("redirect_uri") redirectUrl: String,
                       @Field("app_id") clientId: String,
                       @Field("app_secret") clientSecret: String,
                       @Field("grant_type") grantType: String = GRANT_TYPE,
                       @Header("Content-Type") type: String = CONTENT_TYPE): Single<AccessTokenBean>
}
