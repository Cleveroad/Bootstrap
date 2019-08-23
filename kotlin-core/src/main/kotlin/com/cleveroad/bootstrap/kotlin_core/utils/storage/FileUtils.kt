package com.cleveroad.bootstrap.kotlin_core.utils.storage

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import com.cleveroad.bootstrap.kotlin_core.utils.storage.UriUtils.Companion.isContentScheme
import com.cleveroad.bootstrap.kotlin_core.utils.storage.UriUtils.Companion.isFileScheme
import com.cleveroad.bootstrap.kotlin_core.utils.storage.UriUtils.Companion.isGoogleDrive
import com.cleveroad.bootstrap.kotlin_core.utils.storage.UriUtils.Companion.isGooglePhotos
import com.cleveroad.bootstrap.kotlin_ext.withNotNull
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Provides common methods of the utility for working with file
 */
class FileUtils private constructor() {

    companion object {

        /**
         * Get file's path by uri
         *
         * @param context instance of Context[Context]
         * @param fileUri file's uri[Uri]
         *
         * @return file's path
         */
        @SuppressLint("NewApi")
        fun getRealPath(context: Context, fileUri: Uri?): String? =
                fileUri?.let { uri ->
                    when {
                        // DocumentProvider
                        DocumentsContract.isDocumentUri(context, uri) -> when (uri.authority) {
                            EXTERNAL_STORAGE_PROVIDER -> getExternalStoragePath(uri)
                            DOWNLOADS_PROVIDER -> getDownloadsPath(context, uri)
                            MEDIA_PROVIDER -> getMediaPath(context, uri)
                            else -> getFileName(context, uri)
                        }
                        isContentScheme(uri) -> when {
                            isGooglePhotos(uri) -> uri.lastPathSegment
                            isGoogleDrive(uri) -> copyFile(context, uri, TypeDirPath.CACHE)?.absolutePath
                            else -> getDataColumn(context, uri)
                                    ?: copyFile(context, uri, TypeDirPath.CACHE)?.absolutePath
                        }
                        isFileScheme(uri) -> uri.path
                        else -> getDefaultPath(context, uri)
                    }
                }

        /**
         * Method copy file from Uri to custom path and get new file's path
         *
         * @param context instance of Context[Context]
         * @param fileUri file's uri[Uri]
         * @param pathTo copy path. Default = External public storage with [Environment.DIRECTORY_PICTURES] file name
         *
         * @return file's path
         */
        @SuppressLint("NewApi")
        fun copyFileAndGetRealPath(context: Context,
                                   fileUri: Uri?,
                                   pathTo: String? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)?.absolutePath): String? =
                fileUri?.run { copyFile(context, this, pathTo)?.absolutePath }

        /**
         * Method create new file
         * @param path file path. Default = External storage with [TEMP_FILES_DIR] file name
         * @param deleteIfExist delete file if it already created in path. Default = true
         * @return [File] created file. May return null if path is null
         */
        fun createFile(path: String?,
                       title: String? = null,
                       deleteIfExist: Boolean = true): File? =
                path?.run {
                    File(this, title ?: UUID.randomUUID().toString()).apply {
                        if (deleteIfExist && exists()) {
                            delete()
                            createNewFile()
                        }
                    }
                }

        /**
         * Method create new file
         * @param context [Context]Application context
         * @param dirType [TypeDirPath] type of directory
         * @param deleteIfExist [Boolean] delete file if it already created in path. Default = true
         * @return [File] created file. May return null if storage is not currently available.
         */
        fun createFile(context: Context,
                       dirType: TypeDirPath = TypeDirPath.CACHE,
                       title: String? = UUID.randomUUID().toString(),
                       deleteIfExist: Boolean = true): File? =
                createFile(dirType.path(context), title, deleteIfExist)

        /**
         * Method copy file from Uri to custom path
         * @param context Application context
         * @param fileFrom file uri which should be copied
         * @param pathTo copy path. Default = External storage with [TEMP_FILES_DIR] file name
         * @param deleteIfExist delete file if it already created in path. Default = true
         */
        @Throws(Exception::class)
        fun copyFile(context: Context, fileFrom: Uri,
                     pathTo: String? = context.cacheDir.absolutePath,
                     deleteIfExist: Boolean = true): File? {
            context.contentResolver.openInputStream(fileFrom)?.let { inputStream ->
                BufferedInputStream(inputStream).use { innerInputStream ->
                    withNotNull(innerInputStream) {
                        val originalSize = available()
                        val fileName = getFileName(context, fileFrom)
                        val file = createFile(pathTo, fileName, deleteIfExist)
                        file?.let {
                            BufferedOutputStream(FileOutputStream(it, false)).use { outputStream ->
                                val buf = ByteArray(originalSize)
                                while (read(buf) != -1) {
                                    outputStream.write(buf)
                                }
                                outputStream.flush()
                            }
                        }
                        return file
                    }
                }
            }
            return null
        }

        /**
         * Method copy file from Uri to custom path
         * @param context Application context
         * @param fileFrom file uri which should be copied
         * @param dirType [TypeDirPath.CACHE] = context.cacheDir,
         * [TypeDirPath.EXTERNAL_TEMP] = context.getExternalFilesDir([TEMP_FILES_DIR])
         * @param deleteIfExist delete file if it already created in path. Default = true
         */
        @Throws(Exception::class)
        fun copyFile(context: Context, fileFrom: Uri,
                     dirType: TypeDirPath = TypeDirPath.CACHE,
                     deleteIfExist: Boolean = true): File? =
                copyFile(context, fileFrom, dirType.path(context), deleteIfExist)


        /**
         *  Create directory in the cache path [Context.getCacheDir]
         * @param dirPath [String] directory path
         * @param dirName [String] new directory name, by default it will be [UUID.randomUUID]
         * @param clearIfExist [Boolean] true: if directory has already created, it will be deleted
         */
        fun createDir(dirPath: String?,
                      dirName: String? = null,
                      clearIfExist: Boolean = true) =
                File(dirPath, dirName ?: UUID.randomUUID().toString()).apply {
                    if (clearIfExist) deleteRecursively()
                    if (!exists()) mkdir()
                }

        /**
         *  Create directory in the cache path [Context.getCacheDir]
         * @param context [Context] instance of Context
         * @param dirPath [String] directory path
         * @param dirName [String] new directory name, by default it will be [UUID.randomUUID]
         * @param clearIfExist [Boolean] true: if directory has already created, it will be deleted
         */
        fun createDir(context: Context,
                      dirPath: TypeDirPath = TypeDirPath.CACHE,
                      dirName: String? = UUID.randomUUID().toString(),
                      clearIfExist: Boolean = true) =
                createDir(dirPath.path(context), dirName, clearIfExist)

        /**
         * Deletes the file or directory denoted by this abstract pathname.  If
         * this pathname denotes a directory, then the directory will be deleted with all its children.
         *
         * <p> Note that the {@link java.nio.file.Files} class defines the [java.nio.file.Files.delete]
         * method to throw an [java.io.IOException]
         * when a file cannot be deleted. This is useful for error reporting and to
         * diagnose why a file cannot be deleted.
         *
         * @return  true if and only if the file or directory is
         *          successfully deleted; false otherwise
         *
         * @throws  SecurityException
         *          If a security manager exists and its [java.lang.SecurityManager.checkDelete] method denies
         *          delete access to the file
         */
        fun delete(file: File): Boolean =
                file.takeIf { it.exists() }
                        ?.run { if (isDirectory) deleteRecursively() else delete() }
                        ?: false

        /**
         * See [delete]
         */
        fun delete(path: String): Boolean = delete(File(path))

        /**
         * Gets the extension of a file.
         * @param file
         * @return extension including the dot, or empty if there is no extension.
         */
        fun getExtension(file: File): String? =
                file.takeIf { it.exists() }?.extension
                        ?: file.absolutePath
                                .lastIndexOf(DOT_CHAR)
                                .takeIf { it >= 0 }
                                ?.let { file.absolutePath.substring(it) }
    }
}
