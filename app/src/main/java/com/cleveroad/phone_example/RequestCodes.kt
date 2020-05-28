package com.cleveroad.phone_example

enum class RequestCode {
    REQUEST_CHOOSE_COUNTRY;

    operator fun invoke() = ordinal
}