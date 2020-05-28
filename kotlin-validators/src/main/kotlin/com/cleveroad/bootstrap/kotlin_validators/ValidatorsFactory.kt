package com.cleveroad.bootstrap.kotlin_validators

import android.content.Context
import androidx.annotation.StringRes

/**
 * Class with factory methods for getting different types of preset validators
 */
object ValidatorsFactory {

    fun getNameValidator(context: Context) = NameValidator.getDefaultValidator(context)

    fun getUserNameValidator(context: Context) =
            getUserNameValidator(context, R.string.username_is_empty, R.string.username_is_invalid)

    fun getFirstNameValidator(context: Context) =
            getNameValidator(context, R.string.first_name_is_empty, R.string.first_name_is_invalid)

    fun getLastNameValidator(context: Context) =
            getNameValidator(context, R.string.last_name_is_empty, R.string.last_name_is_invalid)

    fun getLoginValidator(context: Context) =
            getUserNameValidator(context, R.string.login_is_empty, R.string.login_is_invalid)

    fun getInviteCodeValidator(context: Context) =
            InviteCodeValidator.getDefaultValidator(context)

    fun getEmailValidator(context: Context) =
            EmailValidator.getDefaultValidator(context)

    fun getPasswordValidator(context: Context) =
            PasswordValidator.getDefaultValidator(context)

    fun getStrictPasswordValidator(context: Context) = PasswordValidator.build(context) {
        additionalRegex = PasswordValidator.STRICT_PASSWORD_PATTERN.toRegex()
    }

    fun getPhoneValidator(context: Context) =
            PhoneValidatorImpl.getDefault(context)

    fun getDateValidator(context: Context) =
            DateValidatorImpl.getDefaultValidator(context)

    fun getMatchPasswordValidator(context: Context) =
            MatchPasswordValidator.getDefaultValidator(context)


    private fun getNameValidator(context: Context,
                                 @StringRes emptyErrorMessageId: Int,
                                 @StringRes invalidErrorMessageId: Int) =
            NameValidator.builder(context)
                    .emptyErrorMessage(emptyErrorMessageId)
                    .invalidErrorMessage(invalidErrorMessageId)
                    .build()


    private fun getUserNameValidator(context: Context,
                                     @StringRes emptyErrorMessageId: Int,
                                     @StringRes invalidErrorMessageId: Int) =
            NameValidator.builder(context)
                    .emptyErrorMessage(emptyErrorMessageId)
                    .invalidErrorMessage(invalidErrorMessageId)
                    .additionalRegex(NameValidator.USER_NICK_NAME_PATTERN)
                    .build()

}
