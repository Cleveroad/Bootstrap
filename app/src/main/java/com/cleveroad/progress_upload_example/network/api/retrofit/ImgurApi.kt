package com.cleveroad.progress_upload_example.network.api.retrofit

import com.cleveroad.progress_upload_example.network.api.beans.Response
import com.cleveroad.progress_upload_example.network.api.beans.image.UploadedImageBean
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImgurApi {
    @Multipart
    @Headers("Authorization: Client-ID e08f2ada07b6551")
    @POST("image")
    fun uploadImage(@Part image: MultipartBody.Part): Single<Response<UploadedImageBean>>
}