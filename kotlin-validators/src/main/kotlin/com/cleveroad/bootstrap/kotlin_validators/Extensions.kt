package com.cleveroad.bootstrap.kotlin_validators

import android.text.TextUtils


fun String.isDigitsOnly() = TextUtils.isDigitsOnly(this)
