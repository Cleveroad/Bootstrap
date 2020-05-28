package com.cleveroad.progress_upload_example.network.api.beans

import com.fasterxml.jackson.annotation.JsonProperty

data class Response<T>(
    @JsonProperty("success")
    val success: Boolean,
    @JsonProperty("status")
    val status: Int,
    @JsonProperty("data")
    val data: T
)