package com.cleveroad.bootstrap.kotlin_ext

import android.graphics.Bitmap

/**
 * @return bitmap if it is recycled or null
 */
fun Bitmap?.getIfNotRecycled() = takeUnless { it?.isRecycled == true }

/**
 * Check if not null and recycle if not recycled
 */
fun Bitmap?.checkAndRecycle() = withNotNull(this) { if (!isRecycled) recycle() }
