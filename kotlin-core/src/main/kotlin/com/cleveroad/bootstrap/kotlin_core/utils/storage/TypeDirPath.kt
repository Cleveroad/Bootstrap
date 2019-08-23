package com.cleveroad.bootstrap.kotlin_core.utils.storage

import android.content.Context

enum class TypeDirPath {
    CACHE,
    EXTERNAL_TEMP;

    fun path(context: Context): String? =
            when (this) {
                CACHE -> context.cacheDir.absolutePath
                EXTERNAL_TEMP -> context.getExternalFilesDir(TEMP_FILES_DIR)?.absolutePath
            }

}