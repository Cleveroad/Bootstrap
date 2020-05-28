package com.cleveroad.bootstrap.kotlin_validators

import android.content.Context
import androidx.annotation.StringRes
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil


open class PhoneValidatorImpl private constructor(context: Context,
                                                  private val invalidErrorMessage: String,
                                                  private val emptyErrorMessage: String
) : PhoneValidator {

    companion object {
        fun builder(context: Context) = Builder(context)
        fun getDefault(context: Context) = builder(context).build()
        inline fun build(context: Context, block: PhoneValidatorImpl.Builder.() -> Unit) = PhoneValidatorImpl.Builder(context).apply(block).build()
    }

    private val mPhoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.createInstance(context)

    override fun validate(phone: String?) = validate(phone, "")

    override fun validate(phone: String?, countryRegion: String?): ValidationResponse {
        val error = if (phone.isNullOrEmpty()) {
            emptyErrorMessage
        } else {
            try {
                if (!mPhoneNumberUtil.isValidNumber(mPhoneNumberUtil.parse(phone, countryRegion))) {
                    invalidErrorMessage
                } else {
                    ""
                }
            } catch (e: NumberParseException) {
                e.printStackTrace()
                invalidErrorMessage
            }
        }
        return ValidationResponseImpl(error.isEmpty(), error)
    }

    class Builder(aContext: Context) {
        private val context = aContext.applicationContext
        var emptyErrorMessage: String = context.getString(R.string.phone_is_empty)
        var invalidErrorMessage: String = context.getString(R.string.phone_is_invalid)

        fun emptyErrorMessage(message: String?) = apply { emptyErrorMessage = message ?: "" }

        fun emptyErrorMessage(@StringRes id: Int) = emptyErrorMessage(context.getString(id))

        fun invalidErrorMessage(message: String?) = apply { invalidErrorMessage = message ?: "" }

        fun invalidErrorMessage(@StringRes id: Int) = invalidErrorMessage(context.getString(id))

        fun build() = PhoneValidatorImpl(context, invalidErrorMessage, emptyErrorMessage)
    }
}

