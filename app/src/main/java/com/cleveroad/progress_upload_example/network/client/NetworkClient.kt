package com.cleveroad.progress_upload_example.network.client

import com.cleveroad.progress_upload_example.network.api.retrofit.ImgurApi
import com.cleveroad.progress_upload_example.network.modules.ImageModule
import com.cleveroad.progress_upload_example.network.modules.ImageModuleImpl
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class NetworkClient : BaseNetworkClient() {

    companion object {
        private const val TIMEOUT_IN_SECONDS = 30L
    }

    val imageModule: ImageModule by lazy { ImageModuleImpl(retrofit.create(ImgurApi::class.java)) }

    override fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
    }.build()
}