package com.cleveroad.progress_upload_example.network.api.converters

import com.cleveroad.progress_upload_example.models.converters.BaseConverter
import com.cleveroad.progress_upload_example.models.image.UploadedImage
import com.cleveroad.progress_upload_example.models.image.UploadedImageModel
import com.cleveroad.progress_upload_example.network.api.beans.image.UploadedImageBean

class UploadedImageConverter: BaseConverter<UploadedImageBean, UploadedImage>() {

    override fun processConvertInToOut(inObject: UploadedImageBean?): UploadedImage? = inObject?.run {
        UploadedImageModel(link)
    }

    override fun processConvertOutToIn(outObject: UploadedImage?): UploadedImageBean? = outObject?.run {
        UploadedImageBean(link)
    }
}