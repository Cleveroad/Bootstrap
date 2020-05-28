package com.cleveroad.bootstrap.kotlin_validators

import android.content.Context
import androidx.annotation.StringRes
import java.util.regex.Pattern
import kotlin.math.max

open class NameValidator private constructor(private var nameMinLength: Int,
                                             private var nameMaxLength: Int,
                                             private var emptyErrorMessage: String,
                                             private var minLengthErrorMessage: String,
                                             private var maxLengthErrorMessage: String,
                                             private var invalidErrorMessage: String,
                                             private var additionalRegex: Regex? = null) : Validator {

    companion object {

        val USER_NICK_NAME_PATTERN: Pattern = Pattern.compile("[\\p{L}\\p{Nd}-_]+")

        inline fun build(context: Context, block: NameValidator.Builder.() -> Unit) = NameValidator.Builder(context).apply(block).build()

        fun builder(context: Context) = Builder(context)

        fun getDefaultValidator(context: Context) = builder(context).build()
    }

    override fun validate(name: String?): ValidationResponse {

        val error = if (name.isNullOrEmpty()) {
            emptyErrorMessage
        } else if (name.length < nameMinLength) {
            minLengthErrorMessage
        } else if (name.length > nameMaxLength) {
            maxLengthErrorMessage
        } else if (!isNameValid(name)) {
            invalidErrorMessage
        } else {
            ""
        }
        return ValidationResponseImpl(error.isEmpty(), error)
    }

    private fun isNameValid(name: String) = !name.isEmpty()
            && name.length >= nameMinLength
            && name.length <= nameMaxLength
            && checkPatternAndValidate(name)

    private fun checkPatternAndValidate(name: String) = additionalRegex?.matches(name) ?: true

    class Builder(aContext: Context) {
        private val context = aContext.applicationContext
        var nameMinLength: Int = 0
        var nameMaxLength: Int = context.resources.getInteger(R.integer.max_name_length)
        var emptyErrorMessage: String = context.getString(R.string.name_is_empty)
        var minLengthErrorMessage: String = context.getString(R.string.name_is_too_short)
        var maxLengthErrorMessage: String = context.getString(R.string.name_is_too_long)
        var invalidErrorMessage: String = context.getString(R.string.name_is_invalid)
        var additionalRegex: Regex? = null

        fun nameMinLength(length: Int) = apply { if (length > 0) nameMinLength = length }
        fun nameMaxLength(length: Int) = apply { if (length > 0) nameMaxLength = length }

        fun emptyErrorMessage(message: String?) = apply { emptyErrorMessage = message ?: "" }
        fun emptyErrorMessage(@StringRes id: Int) = apply { emptyErrorMessage = context.getString(id) }

        fun minLengthErrorMessage(message: String?) = apply {
            minLengthErrorMessage = message ?: ""
        }

        fun minLengthErrorMessage(@StringRes id: Int) = apply { minLengthErrorMessage = context.getString(id) }

        fun maxLengthErrorMessage(message: String?) = apply {
            maxLengthErrorMessage = message ?: ""
        }

        fun maxLengthErrorMessage(@StringRes id: Int) = apply { maxLengthErrorMessage = context.getString(id) }

        fun invalidErrorMessage(message: String?) = apply { invalidErrorMessage = message ?: "" }
        fun invalidErrorMessage(@StringRes id: Int) = apply { invalidErrorMessage = context.getString(id) }

        fun additionalRegex(regex: String) = apply { additionalRegex = regex.toRegex() }
        fun additionalRegex(regex: Regex) = apply { additionalRegex = regex }
        fun additionalRegex(regex: Pattern) = apply { additionalRegex = regex.toRegex() }

        fun build(): Validator {
            nameMaxLength = max(nameMaxLength, nameMinLength)
            return NameValidator(nameMinLength, nameMaxLength, emptyErrorMessage, minLengthErrorMessage,
                    maxLengthErrorMessage, invalidErrorMessage, additionalRegex)
        }
    }
}
