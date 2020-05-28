package com.cleveroad.validator_example

import android.widget.TextView

fun TextView.addSimpleTextChangedListener(listener: (text: String) -> Unit) =
        object : SimpleTextWatcher(this) {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listener(s.toString())
            }
        }

fun TextView.getStringText() = this.text.toString()

fun TextView.addFocusChangedListener(listener: (isHasFocus: Boolean) -> Unit) = this.setOnFocusChangeListener { _, hasFocus ->
    listener(hasFocus)
}



