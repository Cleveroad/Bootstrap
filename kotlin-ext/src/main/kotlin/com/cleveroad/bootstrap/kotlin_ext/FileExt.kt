package com.cleveroad.bootstrap.kotlin_ext

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * @param context    instance of Context[Context]
 * @param authority the authority of a [FileProvider] defined in a element in your app's manifest
 *
 * @return a content URI for the file
 */
fun File.getUri(context: Context, authority: String): Uri =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context, authority, this)
    } else {
        Uri.fromFile(this)
    }