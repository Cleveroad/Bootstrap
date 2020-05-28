package com.cleveroad.progress_upload_example.network.modules

import com.cleveroad.progress_upload_example.models.image.UploadedImage
import com.cleveroad.progress_upload_example.network.api.beans.image.UploadedImageBean
import com.cleveroad.progress_upload_example.network.api.converters.UploadedImageConverter
import com.cleveroad.progress_upload_example.network.api.retrofit.ImgurApi
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody

interface ImageModule {
    fun uploadImage(image: MultipartBody.Part): Single<UploadedImage?>
}

class ImageModuleImpl(api: ImgurApi): BaseRxModule<ImgurApi, UploadedImageBean, UploadedImage>(api, UploadedImageConverter()),
    ImageModule {

    private val imageBeanConverter by lazy { UploadedImageConverter() }

    override fun uploadImage(image: MultipartBody.Part): Single<UploadedImage?> =
        api.uploadImage(image)
            .onErrorResumeNext(NetworkErrorUtils.rxParseSingleError())
            .map { it.data }
            .compose(imageBeanConverter.single.inToOut())
}