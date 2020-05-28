package com.cleveroad.bootstrap.kotlin_ext

import android.net.Uri
import java.io.File

/**
 * Check whether "contentUri" is real file path (not content uri)
 *
 * @return possible file path
 */
fun Uri.checkRealFilePath() = this.path?.takeIf { File(it).exists() }
