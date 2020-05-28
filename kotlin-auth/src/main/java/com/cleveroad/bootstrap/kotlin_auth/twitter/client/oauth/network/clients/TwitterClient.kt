package com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.clients

import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.TWITTER_API_ENDPOINT
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.api.TwitterOAuthApi
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.interceptors.OAuth1SigningInterceptor
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.modules.TwitterOAuthModule
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.modules.TwitterOAuthModuleImpl
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

class TwitterClient {

    companion object {
        private const val TIMEOUT_IN_SECONDS = 30L
    }

    private val mapper: ObjectMapper = ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(createHttpClient())
            .baseUrl(TWITTER_API_ENDPOINT)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .build()

    val twitterOAuth: TwitterOAuthModule by lazy {
        TwitterOAuthModuleImpl(retrofit.create(TwitterOAuthApi::class.java))
    }

    private fun createHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(LoggingInterceptor.Builder()
                        .loggable(true)
                        .setLevel(Level.BASIC)
                        .log(Platform.INFO)
                        .request("Request>>>>")
                        .response("Response<<<<")
                        .build())
                .addInterceptor(OAuth1SigningInterceptor())
    }.build()
}