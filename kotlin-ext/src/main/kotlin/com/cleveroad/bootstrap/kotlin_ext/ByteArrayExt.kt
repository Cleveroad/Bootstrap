package com.cleveroad.bootstrap.kotlin_ext

import android.util.Base64
import java.io.File
import java.io.FileOutputStream

fun ByteArray.toFile(file: File) = file.also { FileOutputStream(it).use { stream -> stream.write(this) } }

fun ByteArray?.toBase64(encoderFlag: Int = Base64.NO_WRAP) = this?.let { Base64.encodeToString(it, encoderFlag) }