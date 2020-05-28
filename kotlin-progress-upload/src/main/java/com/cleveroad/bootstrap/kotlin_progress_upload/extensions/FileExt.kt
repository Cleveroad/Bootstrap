package com.cleveroad.bootstrap.kotlin_progress_upload.extensions

import com.cleveroad.bootstrap.kotlin_progress_upload.counting.CountingProgressCallback
import com.cleveroad.bootstrap.kotlin_progress_upload.counting.CountingRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

private const val FORM_DATA_NAME = "file"
private const val FORM_DATA_TYPE = "multipart/form-data"

/**
 * Create [MultipartBody.Part] from file and wraps it in [CountingRequestBody] to track progress.
 *
 * @param formDataName [String] form data name, e.g. "image".
 * @param formDataType [String] form data type.
 * @param countingCallback [CountingProgressCallback] class which will track progress.
 *
 * @return [MultipartBody.Part] created multipart body.
 */
fun File.getMultipartCounting(formDataName: String = FORM_DATA_NAME,
                              formDataType: String = FORM_DATA_TYPE,
                              countingCallback: CountingProgressCallback): MultipartBody.Part {
    val filePart = RequestBody.create(MediaType.parse(formDataType), this)
    return MultipartBody.Part.createFormData(formDataName, name, CountingRequestBody(filePart, countingCallback))
}