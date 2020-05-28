package com.cleveroad.validator_example

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView


open class SimpleTextWatcher(textView: TextView) : TextWatcher {
    init {
        textView.addTextChangedListener(this)
    }

    override fun afterTextChanged(s: Editable?) = Unit

    override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
}