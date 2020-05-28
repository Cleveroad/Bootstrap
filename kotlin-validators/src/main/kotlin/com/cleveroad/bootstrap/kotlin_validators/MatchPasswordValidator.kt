package com.cleveroad.bootstrap.kotlin_validators

import android.content.Context
import androidx.annotation.StringRes
import java.util.regex.Pattern

@Suppress("unused")
class MatchPasswordValidator private constructor() : MatchValidator {

    companion object {
        const val SUCCESS = 0
        const val PASSWORD_ERROR_FIELD = 1
        const val CONFIRM_PASSWORD_ERROR_FIELD = 2
        inline fun build(context: Context, block: MatchPasswordValidator.Builder.() -> Unit) = MatchPasswordValidator.Builder(context).apply(block).build()

        fun builder(context: Context) = Builder(context)

        fun getDefaultValidator(context: Context) = builder(context).build()

    }

    private var passwordValidator: Validator? = null
    var confirmPasswordValidator: Validator? = null
    var notMatchError: String? = null

    override fun validate(password: String?,
                          confirmPassword: String?): MatchValidationResponse {
        val passwordResponse = passwordValidator?.validate(password)
        if (passwordResponse?.isValid == false) {
            return MatchValidationResponseImpl(passwordResponse, PASSWORD_ERROR_FIELD)
        }
        val confirmPasswordResponse = confirmPasswordValidator?.validate(confirmPassword)
        if (confirmPasswordResponse?.isValid == false) {
            return MatchValidationResponseImpl(confirmPasswordResponse, CONFIRM_PASSWORD_ERROR_FIELD)
        }
        return if (password != confirmPassword) {
            MatchValidationResponseImpl(false, notMatchError, CONFIRM_PASSWORD_ERROR_FIELD)
        } else MatchValidationResponseImpl(true, null, SUCCESS)
    }

    class Builder constructor(aContext: Context) {

        private val context: Context = aContext.applicationContext
        private val matchPasswordValidator: MatchPasswordValidator = MatchPasswordValidator().apply {
            notMatchError = context.getString(R.string.passwords_dont_match)
        }
        private val passwordValidatorBuilder: PasswordValidator.Builder = PasswordValidator.builder(context)
        private val confirmPasswordValidatorBuilder = PasswordValidator.builder(context)
                .emptyErrorMessage(R.string.confirm_empty)
                .invalidErrorMessage(R.string.confirm_password_is_invalid)


        fun minLengthErrorMessage(message: String?) = apply {
            this.passwordValidatorBuilder.minLengthErrorMessage = message ?: ""
        }

        fun minLengthErrorMessage(@StringRes id: Int) = apply {
            passwordValidatorBuilder.minLengthErrorMessage = context.getString(id)
        }

        fun maxLengthErrorMessage(message: String?) = apply {
            passwordValidatorBuilder.maxLengthErrorMessage = message ?: ""
        }

        fun maxLengthErrorMessage(@StringRes id: Int) = apply {
            passwordValidatorBuilder.maxLengthErrorMessage = context.getString(id)
        }

        fun minLengthConfirmErrorMessage(message: String?) = apply {
            this.confirmPasswordValidatorBuilder.minLengthErrorMessage = message ?: ""
        }

        fun minLengthConfirmErrorMessage(@StringRes id: Int) = apply {
            confirmPasswordValidatorBuilder.minLengthErrorMessage = context.getString(id)
        }

        fun maxLengthConfirmErrorMessage(message: String?) = apply {
            confirmPasswordValidatorBuilder.maxLengthErrorMessage = message ?: ""
        }

        fun maxLengthConfirmErrorMessage(@StringRes id: Int) = apply {
            confirmPasswordValidatorBuilder.maxLengthErrorMessage = context.getString(id)
        }

        fun passwordEmptyErrorMessage(message: String?) = apply {
            passwordValidatorBuilder.emptyErrorMessage = message ?: ""
        }

        fun passwordEmptyErrorMessage(@StringRes id: Int) = apply {
            passwordValidatorBuilder.emptyErrorMessage = context.getString(id)
        }

        fun passwordInvalidErrorMessage(message: String?) = apply {
            passwordValidatorBuilder.invalidErrorMessage = message ?: ""
        }

        fun passwordInvalidErrorMessage(@StringRes id: Int) = apply {
            passwordValidatorBuilder.invalidErrorMessage = context.getString(id)
        }

        fun confirmPasswordEmptyErrorMessage(message: String?) = apply {
            confirmPasswordValidatorBuilder.emptyErrorMessage = message ?: ""
        }

        fun confirmPasswordEmptyErrorMessage(@StringRes id: Int) = apply {
            confirmPasswordValidatorBuilder.emptyErrorMessage = context.getString(id)
        }

        fun confirmPasswordInvalidErrorMessage(message: String?) = apply {
            confirmPasswordValidatorBuilder.invalidErrorMessage = message ?: ""
        }

        fun confirmPasswordInvalidErrorMessage(@StringRes id: Int) = apply {
            confirmPasswordValidatorBuilder.invalidErrorMessage = context.getString(id)
        }

        fun notMatchErrorMessage(message: String?) = apply {
            matchPasswordValidator.notMatchError = message ?: ""
        }

        fun notMatchErrorMessage(@StringRes id: Int) = apply {
            matchPasswordValidator.notMatchError = context.getString(id)
        }

        fun additionalRegex(additionalRegex: Pattern) = apply {
            passwordValidatorBuilder.additionalRegex(additionalRegex)
            confirmPasswordValidatorBuilder.additionalRegex(additionalRegex)
        }

        fun additionalRegex(additionalRegex: String) = apply {
            passwordValidatorBuilder.additionalRegex(additionalRegex)
            confirmPasswordValidatorBuilder.additionalRegex(additionalRegex)
        }

        fun additionalRegex(additionalRegex: Regex) = apply {
            passwordValidatorBuilder.additionalRegex(additionalRegex)
            confirmPasswordValidatorBuilder.additionalRegex(additionalRegex)
        }

        fun maxLength(length: Int) = apply {
            passwordValidatorBuilder.passwordMaxLength(length)
            confirmPasswordValidatorBuilder.passwordMaxLength(length)
        }

        fun minLength(length: Int) = apply {
            passwordValidatorBuilder.passwordMinLength(length)
            confirmPasswordValidatorBuilder.passwordMinLength(length)
        }

        fun build() = matchPasswordValidator.apply {
            passwordValidator = passwordValidatorBuilder.build()
            confirmPasswordValidator = confirmPasswordValidatorBuilder.build()
        }
    }
}
