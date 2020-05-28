package com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.clients

import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.INSTAGRAM_ENDPOINT
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.api.InstagramAuthApi
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.modules.OAuthModule
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.network.modules.OAuthModuleImpl
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

class InstagramClient {

    companion object {
        private const val TIMEOUT_IN_SECONDS = 30L
    }

    private val mapper: ObjectMapper = ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(createHttpClient())
            .baseUrl(INSTAGRAM_ENDPOINT)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .build()

    val instagramOAuth: OAuthModule by lazy {
        OAuthModuleImpl(retrofit.create(InstagramAuthApi::class.java))
    }

    private fun createHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(LoggingInterceptor.Builder()
                    .loggable(true)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("Request>>>>")
                    .response("Response<<<<")
                    .build())
            .build()
}