package com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.network.api

import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.CONTENT_TYPE
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.GRANT_TYPE
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.network.beans.AccessTokenBean
import io.reactivex.Single
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LinkedInAuthApi {

    @POST("/oauth/v2/accessToken")
    fun getAccessToken(@Query("code") code: String,
                       @Query("redirect_uri") redirectUrl: String,
                       @Query("client_id") clientId: String,
                       @Query("client_secret") clientSecret: String,
                       @Query("grant_type") grantType: String = GRANT_TYPE,
                       @Header("Content-Type") type: String = CONTENT_TYPE): Single<AccessTokenBean>
}