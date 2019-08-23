package com.cleveroad.bootstrap.kotlin_ext

import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView

/**
 * @return the bitmap used by view drawable to render
 */
fun ImageView.getBitmap() = (this.drawable as? BitmapDrawable)?.bitmap
