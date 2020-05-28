package com.cleveroad.bootstrap.kotlin_progress_upload.counting

import okhttp3.RequestBody
import okio.*

import java.io.IOException
import java.lang.ref.WeakReference

/**
 * Counting request body, which allow you to track file upload progress.
 *
 * @param countingCallback [CountingProgressCallback] class which will process upload progress.
 * @property requestBody [RequestBody] request body created from file.
 */
class CountingRequestBody(private val requestBody: RequestBody, countingCallback: CountingProgressCallback) :
    RequestBody() {

    companion object {
        private const val NO_CONTENT = -1L
        private const val ONE_HUNDRED_PERCENT = 100F
    }

    private val countingWR = WeakReference(countingCallback)

    /**
     * Get request body content type.
     *
     * @return [MediaType] content type.
     */
    override fun contentType() = requestBody.contentType()

    /**
     * Get request body content length in bytes.
     *
     * @return [Long] content length.
     */
    override fun contentLength(): Long = try {
        requestBody.contentLength()
    } catch (exception: IOException) {
        NO_CONTENT
    }

    /**
     * Writes the content of this request to [sink].
     *
     * @param sink [BufferedSink] current request body sink.
     */
    override fun writeTo(sink: BufferedSink) {
        CountingSink(sink).buffer().apply {
            requestBody.writeTo(this)
            flush()
        }
    }

    /**
     * Wrapper for [BufferedSink], used to track bytes upload progress.
     *
     * @param delegate [Sink] current request body sink.
     */
    internal inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {

        private var bytesWritten: Long = 0

        /**
         * Removes [byteCount] bytes from [source] and appends them to [bytesWritten].
         *
         * @param source [Buffer] current source.
         * @param byteCount [Long] current bytes written.
         */
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            bytesWritten += byteCount
            countingWR.get()?.onProgressChanged(bytesWritten * ONE_HUNDRED_PERCENT / contentLength())
        }
    }
}