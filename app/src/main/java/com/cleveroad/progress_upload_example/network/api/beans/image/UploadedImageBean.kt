package com.cleveroad.progress_upload_example.network.api.beans.image

import com.fasterxml.jackson.annotation.JsonProperty

data class UploadedImageBean(@JsonProperty("link")
                             val link: String?)