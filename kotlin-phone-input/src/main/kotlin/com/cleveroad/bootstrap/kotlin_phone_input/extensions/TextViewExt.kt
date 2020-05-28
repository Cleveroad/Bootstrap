package com.cleveroad.bootstrap.kotlin_phone_input.extensions

import android.graphics.drawable.Drawable
import android.widget.TextView

fun TextView?.setDrawable(start: Drawable? = null, top: Drawable? = null, end: Drawable? = null, bottom: Drawable? = null) {
    this?.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
}
