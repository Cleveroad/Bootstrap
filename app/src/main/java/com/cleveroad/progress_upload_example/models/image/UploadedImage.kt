package com.cleveroad.progress_upload_example.models.image

import com.cleveroad.progress_upload_example.models.Model
import kotlinx.android.parcel.Parcelize

interface UploadedImage : Model {
    var link: String?
}

@Parcelize
data class UploadedImageModel(override var link: String?) : UploadedImage