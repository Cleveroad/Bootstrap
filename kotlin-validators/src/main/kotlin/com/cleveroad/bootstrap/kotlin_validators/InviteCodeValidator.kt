package com.cleveroad.bootstrap.kotlin_validators


import android.content.Context
import java.util.regex.Pattern

open class InviteCodeValidator private constructor(private val minLength: Int,
                                                   private val maxLength: Int,
                                                   private val emptyError: String,
                                                   private val invalidError: String,
                                                   private val additionalRegex: Regex?) : Validator {

    companion object {

        inline fun build(context: Context, block: InviteCodeValidator.Builder.() -> Unit) = InviteCodeValidator.Builder(context).apply(block).build()

        fun builder(context: Context) = InviteCodeValidator.Builder(context)

        fun getDefaultValidator(context: Context) = builder(context).build()

    }

    override fun validate(code: String?): ValidationResponse {
        val error = if (code.isNullOrEmpty()) emptyError else if (!isCodeValid(code)) invalidError else ""
        return ValidationResponseImpl(error.isEmpty(), error)
    }

    private fun isCodeValid(code: String) = !code.isEmpty()
            && code.length >= minLength && code.length <= maxLength
            && checkPatternAndValidate(code)

    private fun checkPatternAndValidate(code: String) = additionalRegex?.matches(code) ?: true

    class Builder constructor(aContext: Context) {
        private val context = aContext.applicationContext
        var minLength: Int = context.resources.getInteger(R.integer.min_code_length)
        var maxLength: Int = context.resources.getInteger(R.integer.max_code_length)
        var emptyError: String = context.getString(R.string.code_is_empty)
        var invalidError: String = context.getString(R.string.code_is_invalid)
        var additionalRegex: Regex? = null

        fun minLength(length: Int) = apply { if (length > 0) minLength = length }

        fun maxLength(length: Int) = apply { if (length > 0) maxLength = length }

        fun emptyError(error: String?) = apply {
            emptyError = error ?: ""
        }

        fun invalidError(error: String?) = apply {
            invalidError = error ?: ""
        }

        fun additionalRegex(additionalRegex: Regex?) = apply { this.additionalRegex = additionalRegex }

        fun additionalRegex(additionalRegex: String?) = apply { this.additionalRegex = additionalRegex?.toRegex() }

        fun additionalRegex(additionalRegex: Pattern?) = apply { this.additionalRegex = additionalRegex?.toRegex() }

        fun build(): Validator {
            maxLength = Math.max(maxLength, minLength)
            return InviteCodeValidator(minLength, maxLength, emptyError, invalidError, additionalRegex)
        }
    }
}
