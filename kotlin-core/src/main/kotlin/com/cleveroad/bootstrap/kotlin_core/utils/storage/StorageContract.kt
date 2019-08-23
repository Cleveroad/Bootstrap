package com.cleveroad.bootstrap.kotlin_core.utils.storage

internal const val PRIMARY = "primary"
internal const val SELECTION_BY_DOCUMENT_ID = "_id=?"
internal const val CONTENT = "content"
internal const val FILE = "file"
internal const val COLUMN_DATA = "_data"
internal const val REGEX_FOR_SEPARATING_COLUMNS_OF_DOCUMENT = ":"
internal const val NUMBER_COLUMN_TYPE_DOCUMENT = 0
internal const val NUMBER_COLUMN_PATH_DOCUMENT = 1

/** Providers from uri.  */
internal const val EXTERNAL_STORAGE_PROVIDER = "com.android.externalstorage.documents"
internal const val DOWNLOADS_PROVIDER = "com.android.providers.downloads.documents"
internal const val MEDIA_PROVIDER = "com.android.providers.media.documents"
internal const val GOOGLE_PHOTOS_PROVIDER = "com.google.android.apps.photos.content"
internal const val GOOGLE_DRIVE_PROVIDER = "com.google.android.apps.docs.storage.legacy"
/** Content. */
internal const val PUBLIC_DOWNLOADS_PATH = "content://downloads/public_downloads"
/** Uri scheme. */
internal const val CONTENT_SCHEME = "content"
internal const val FILE_SCHEME = "file"
private const val FILE_PROVIDER = ".provider"
internal const val EXTERNAL_DATA = "external"
internal const val RAW_DATA = "raw:"
/** Common */
internal const val DOT_CHAR = "."
internal const val ID_SUFFIX = "=?"
internal const val EMPTY_STRING_VALUE = ""
internal const val ID_FULL_SUFFIX = "_id=?"
internal const val MIN_COLUMNS_TO_SEPARATE = 2
internal const val TEMP_FILES_DIR = "temp_files"

/** Images */
internal const val TYPE_FILE_IMAGE = "image/*"
internal const val MAX_IMAGE_SIZE = 1024.0 * 1024.0
internal const val MAX_IMAGE_RESOLUTION = 1024
internal const val COMPRESS_QUALITY = 90
internal const val DATE_PATTERN = "yyyyMMdd_HHmmss"
internal const val RESOLUTION_REDUCTION_COEFFICIENT = 2
internal const val FLAG_NOT_MODIFY_DATA_RETURN = 0
internal const val IN_SAMPLE_SIZE = 1
internal const val IMAGE_FORMAT_JPG = ".jpg"