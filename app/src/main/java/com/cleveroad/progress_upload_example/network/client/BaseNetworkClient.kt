package com.cleveroad.progress_upload_example.network.client

import com.cleveroad.progress_upload_example.network.modules.NetworkModule
import com.cleveroad.bootstrap.kotlin_progress_upload.BuildConfig.BASE_URL
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

abstract class BaseNetworkClient {

    protected abstract fun createOkHttpClient(): OkHttpClient

    protected open var mapper: ObjectMapper = NetworkModule.mapper

    protected open val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .build()
    }
}
