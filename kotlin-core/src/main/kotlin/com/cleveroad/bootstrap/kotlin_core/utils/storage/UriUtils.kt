package com.cleveroad.bootstrap.kotlin_core.utils.storage

import android.net.Uri

class UriUtils private constructor() {

    companion object {

        /**
         * Checks whether the Uri authority is External Storage Provider.
         * @param uri to check.
         * @return true if it is, false otherwise.
         */
        fun isExternalStorage(uri: Uri) = EXTERNAL_STORAGE_PROVIDER.equals(uri.authority, ignoreCase = true)

        /**
         * Checks whether the Uri authority is Downloads Provider.
         * @param uri to check.
         * @return true if it is, false otherwise.
         */
        fun isDownloads(uri: Uri) = DOWNLOADS_PROVIDER.equals(uri.authority, ignoreCase = true)

        /**
         * Checks whether the Uri authority is Media Provider.
         * @param uri to check.
         * @return true if it is, false otherwise.
         */
        fun isMedia(uri: Uri) = MEDIA_PROVIDER.equals(uri.authority, ignoreCase = true)

        /**
         * Checks whether the Uri authority is Google Photos.
         * @param uri to check.
         * @return true if it is, false otherwise.
         */
        fun isGooglePhotos(uri: Uri) = GOOGLE_PHOTOS_PROVIDER.equals(uri.authority, ignoreCase = true)

        /**
         * Checks whether the Uri authority is Google Drive.
         * @param uri to check.
         * @return true if it is, false otherwise.
         */
        fun isGoogleDrive(uri: Uri) = GOOGLE_DRIVE_PROVIDER.equals(uri.authority, ignoreCase = true)

        /**
         * Checks whether the Uri scheme is Content.
         * @param uri to check.
         * @return true if it is, false otherwise.
         */
        fun isContentScheme(uri: Uri) = CONTENT_SCHEME.equals(uri.scheme, ignoreCase = true)

        /**
         * Checks whether the Uri scheme is File.
         * @param uri to check.
         * @return true if it is, false otherwise.
         */
        fun isFileScheme(uri: Uri) = FILE_SCHEME.equals(uri.scheme, ignoreCase = true)

    }
}