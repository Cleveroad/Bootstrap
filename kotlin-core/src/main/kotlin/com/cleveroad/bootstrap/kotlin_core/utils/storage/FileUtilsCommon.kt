package com.cleveroad.bootstrap.kotlin_core.utils.storage

import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.cleveroad.bootstrap.kotlin_core.utils.storage.TypeMediaFile.*
import com.cleveroad.bootstrap.kotlin_core.utils.storage.UriUtils.Companion.isContentScheme
import java.io.File

/**
 * Get external storage provider path by uri[Uri]
 *
 * @param uri file's uri[Uri]
 *
 * @return external storage documents path
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
internal fun getExternalStoragePath(uri: Uri): String? {
    return separateColumnsOfDocument(DocumentsContract.getDocumentId(uri))
            .takeIf { PRIMARY.equals(it[NUMBER_COLUMN_TYPE_DOCUMENT], ignoreCase = true) }
            ?.let { "${Environment.getExternalStorageDirectory()}/${it[NUMBER_COLUMN_PATH_DOCUMENT]}" }
}

/**
 * Get Uri path from Downloads Provider.
 * @param context of the caller.
 * @param uri to parse.
 * @return uri path.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
internal fun getDownloadsPath(context: Context, uri: Uri): String? =
        getDocumentId(uri)?.let { docId ->
            if (docId.startsWith(RAW_DATA)) {
                return docId.replaceFirst(RAW_DATA.toRegex(), EMPTY_STRING_VALUE)
            }
            return try {
                val contentUri = ContentUris.withAppendedId(Uri.parse(PUBLIC_DOWNLOADS_PATH), java.lang.Long.valueOf(docId))
                val filePath = getDefaultPath(context, contentUri)?.takeIf { it != docId }
                val filePathAlternative = getDisplayName(context, uri, Environment.DIRECTORY_DOWNLOADS)
                when {
                    filePath != null && File(filePath).exists() -> filePath
                    filePathAlternative != null && File(filePathAlternative).exists() -> filePathAlternative
                    else -> null
                }
            } catch (e: NumberFormatException) {
                uri.toString()
            }
        }

/**
 * Get Uri path from Media Provider.
 * @param context of the caller.
 * @param uri to parse.
 * @return uri path.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
internal fun getMediaPath(@NonNull context: Context, @NonNull uri: Uri): String? =
        getColumnsByDocumentId(uri)?.let { split ->
            val contentUri: Uri
            val column: String
            val selection: String
            when (TypeMediaFile.byValue(split[NUMBER_COLUMN_TYPE_DOCUMENT].toLowerCase())) {
                IMAGE -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    column = MediaStore.Images.Media.DATA
                    selection = MediaStore.Images.Media._ID + ID_SUFFIX
                }
                VIDEO -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    column = MediaStore.Video.Media.DATA
                    selection = MediaStore.Video.Media._ID + ID_SUFFIX
                }
                AUDIO -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    column = MediaStore.Audio.Media.DATA
                    selection = MediaStore.Audio.Media._ID + ID_SUFFIX
                }
                else -> {
                    contentUri = MediaStore.Files.getContentUri(EXTERNAL_DATA)
                    column = COLUMN_DATA
                    selection = ID_FULL_SUFFIX
                }
            }
            getDataColumn(context, contentUri, column, selection, arrayOf(split[NUMBER_COLUMN_PATH_DOCUMENT]))
        }

/**
 * Get the value of the data column for this Uri. This is useful for
 * MediaStore Uris, and other file-based ContentProviders.
 *
 * @param context       instance of Context[Context]
 * @param uri           The Uri to query.
 * @param selection     (Optional) Filter used in the query.
 * @param selectionArgs (Optional) Selection arguments used in the query.
 *
 * @return The value of the [projection] column, which is typically a file path.
 */
internal fun getDataColumn(context: Context,
                           uri: Uri,
                           projection: String = MediaStore.MediaColumns.DATA,
                           selection: String? = null,
                           selectionArgs: Array<String>? = null): String? =
        try {
            context
                    .contentResolver
                    .query(uri, arrayOf(projection), selection, selectionArgs, null)
                    .use { cursor ->
                        cursor?.takeIf { it.moveToFirst() }
                                ?.getString(cursor.getColumnIndexOrThrow(projection))
                    }
        } catch (exc: Throwable) {
            null
        }

@TargetApi(Build.VERSION_CODES.KITKAT)
internal fun getDocumentId(uri: Uri): String? =
        DocumentsContract.getDocumentId(uri).takeIf { it.isNotEmpty() }

/**
 * Get Uri path from content resolver.
 * @param context of the caller.
 * @param uri to parse.ё
 * @return uri path.
 */
internal fun getDefaultPath(context: Context, uri: Uri): String? =
        getDataColumn(context, uri)?.takeUnless { it.isEmpty() }
                ?: getFileName(context, uri)

/**
 * Get Uri path from downloads directory with display name.
 * @param context of the caller.
 * @param uri to parse.
 * @param environment [Environment.DIRECTORY_DOWNLOADS] by default
 * @return uri path.
 */
internal fun getDisplayName(context: Context, uri: Uri, environment: String = Environment.DIRECTORY_DOWNLOADS): String? =
        getDataColumn(context, uri, OpenableColumns.DISPLAY_NAME).let { name ->
            Environment
                    .getExternalStoragePublicDirectory(environment)
                    .absolutePath.plus(File.separator)
                    .plus(name)
                    .takeIf { File(it).exists() }
        }

internal fun separateColumnsOfDocument(document: String) =
        document.split(REGEX_FOR_SEPARATING_COLUMNS_OF_DOCUMENT.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

internal fun getColumnsByDocumentId(uri: Uri): Array<String>? =
        getDocumentId(uri)?.let { documentId ->
            separateColumnsOfDocument(documentId).takeUnless { it.size < MIN_COLUMNS_TO_SEPARATE }
        }

/**
 * Get file name.
 * @param context of the caller.
 * @param uri to parse.
 * @return file name or null if unknown.
 */
internal fun getFileName(context: Context, uri: Uri): String? {
    var fileName: String? = null
    if (isContentScheme(uri)) fileName = getDataColumn(context, uri, OpenableColumns.DISPLAY_NAME)
    return fileName ?: uri.lastPathSegment
}