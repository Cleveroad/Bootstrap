package com.cleveroad.bootstrap.kotlin_validators


import android.content.Context
import android.util.Patterns
import androidx.annotation.StringRes
import java.util.regex.Pattern

open class EmailValidator private constructor(private val emptyError: String,
                                              private val invalidError: String,
                                              private val additionalRegex: Regex?) : Validator {

    companion object {

        inline fun build(context: Context, block: Builder.() -> Unit) = Builder(context).apply(block).build()

        fun builder(context: Context) = Builder(context)

        fun getDefaultValidator(context: Context) = builder(context).build()
    }

    override fun validate(email: String?): ValidationResponse {
        val error = if (email.isNullOrEmpty()) emptyError else if (!isEmailValid(email)) invalidError else ""
        return ValidationResponseImpl(error.isEmpty(), error)
    }

    private fun isEmailValid(email: String): Boolean {
        return !(email.isEmpty() || email.isDigitsOnly())
                && email.matches(Patterns.EMAIL_ADDRESS.toRegex())
                && additionalCheck(email)
    }

    private fun additionalCheck(email: String) = additionalRegex?.matches(email) ?: true

    class Builder(aContext: Context) {
        private val context = aContext.applicationContext
        var emptyError: String = context.getString(R.string.email_is_empty)
        var invalidError: String = context.getString(R.string.email_is_invalid)
        var additionalRegex: Regex? = null

        fun emptyError(emptyError: String?) = apply {
            this.emptyError = emptyError ?: ""
        }

        fun emptyError(@StringRes emptyError: Int) = apply {
            this.emptyError = context.getString(emptyError)
        }

        fun invalidError(invalidError: String?) = apply {
            this.invalidError = invalidError ?: ""
        }

        fun invalidError(@StringRes invalidError: Int) = apply {
            this.invalidError = context.getString(invalidError)
        }

        fun additionalRegex(additionalRegex: Regex?) = apply {
            this.additionalRegex = additionalRegex
        }

        fun additionalRegex(additionalRegex: String?) = apply {
            this.additionalRegex = additionalRegex?.toRegex()
        }

        fun additionalRegex(additionalRegex: Pattern?) = apply {
            this.additionalRegex = additionalRegex?.toRegex()
        }

        fun build() = EmailValidator(emptyError, invalidError, additionalRegex)
    }
}
