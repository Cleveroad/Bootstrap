package com.cleveroad.phone_example

import android.content.Context
import com.cleveroad.bootstrap.kotlin_validators.PhoneValidatorImpl
import com.cleveroad.bootstrap.kotlin_validators.R

fun getPhoneValidator(context: Context) = with(context) {
    PhoneValidatorImpl
            .builder(this)
            .emptyErrorMessage(getString(R.string.phone_is_invalid))
            .invalidErrorMessage(getString(R.string.phone_is_invalid))
            .build()
}
