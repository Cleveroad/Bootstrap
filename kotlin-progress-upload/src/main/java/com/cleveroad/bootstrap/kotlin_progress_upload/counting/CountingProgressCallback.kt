package com.cleveroad.bootstrap.kotlin_progress_upload.counting

/**
 * Implement this interface in the class in which you want to process upload progress.
 */
interface CountingProgressCallback {

    /**
     * Process the upload progress.
     *
     * @param progress [Float] current progress in percentage.
     */
    fun onProgressChanged(progress: Float)
}