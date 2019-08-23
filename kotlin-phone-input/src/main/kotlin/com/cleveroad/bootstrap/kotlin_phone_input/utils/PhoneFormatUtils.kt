package com.cleveroad.bootstrap.kotlin_phone_input.utils

import com.cleveroad.bootstrap.kotlin_phone_input.Constants.EMPTY_STRING
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAsset
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

internal object PhoneFormatUtils {

    fun isPhoneValid(phone: String, country: CountryAsset): Boolean =
            PhoneNumberUtil.getInstance()?.run {
                try {
                    isValidNumber(parse(phone, country.ab))
                } catch (e: NumberParseException) {
                    null
                }
            } ?: false

    @Throws(NumberParseException::class)
    fun formatPhone(countryCode: String,
                    ignoreCountryCode: Boolean = false): String? =
            PhoneNumberUtil.getInstance()?.run {
                getExampleNumber(countryCode)?.let {
                    val example = example(countryCode)
                    val parsed = try {
                        parse(example, countryCode)
                    } catch (numberParseException: NumberParseException) {
                        null
                    } catch (illegalStateException: IllegalStateException) {
                        null
                    }
                    val formatted = format(parsed, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
                    formatted?.run {
                                takeIf { ignoreCountryCode }?.run {
                                    replaceFirst("+${getCountryCodeForRegion(countryCode)}", EMPTY_STRING)
                                } ?: this
                            }
                }
            }

    private fun PhoneNumberUtil.example(countryCode: String): String {
        return getExampleNumberForType(countryCode, PhoneNumberUtil.PhoneNumberType.MOBILE)
                .nationalNumber
                .toString()
    }

    fun getDialCode(country: String) =
            PhoneNumberUtil.getInstance()?.getCountryCodeForRegion(country)?.toLong() ?: 0L
}