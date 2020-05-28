package com.cleveroad.progress_upload_example.ui

enum class RequestCode {
    GALLERY_REQUEST_CODE;

    operator fun invoke() = ordinal
}