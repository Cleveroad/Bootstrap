package com.cleveroad.bootstrap.kotlin_phone_input.utils

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import com.cleveroad.bootstrap.kotlin_phone_input.Constants.DEFAULT_COUNTRY_CODE
import com.cleveroad.bootstrap.kotlin_phone_input.Constants.DEFAULT_COUNTRY_CODE_REGION
import com.cleveroad.bootstrap.kotlin_phone_input.Constants.EMPTY_STRING
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAssetModel


fun getDefaultCountryAsset(context: Context, isInEditMode: Boolean) = run {
    val (country, code) = if (isInEditMode) {
        DEFAULT_COUNTRY_CODE to DEFAULT_COUNTRY_CODE_REGION
    } else {
        getDetectedCountry(context, DEFAULT_COUNTRY_CODE).toUpperCase().let {
            it to PhoneFormatUtils.getDialCode(it)
        }
    }
    CountryAssetModel(country, code, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING)
}

fun getDetectedCountry(context: Context, defaultCountryIsoCode: String) = run {
    val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    detectSIMCountry(telephonyManager)
            ?: detectNetworkCountry(telephonyManager)
            ?: detectLocaleCountry(context)
            ?: defaultCountryIsoCode
}

private fun detectSIMCountry(telephonyManager: TelephonyManager): String? =
        try {
            telephonyManager.simCountryIso.takeIf { it.isNotEmpty() }
        } catch (e: RuntimeException) {
            Log.e("Exception", e.message, e)
            null
        }

private fun detectNetworkCountry(telephonyManager: TelephonyManager): String? =
        try {
            telephonyManager.networkCountryIso.takeIf { it.isNotEmpty() }
        } catch (e: RuntimeException) {
            Log.e("Exception", e.message, e)
            null
        }

private fun detectLocaleCountry(context: Context): String? =
        context.resources?.configuration?.run {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    locales.get(0).country.takeIf { it.isNotEmpty() }
                } else {
                    locale.country.takeIf { it.isNotEmpty() }
                }
            } catch (e: Exception) {
                Log.e("Exception", e.message, e)
                null
            }
        }