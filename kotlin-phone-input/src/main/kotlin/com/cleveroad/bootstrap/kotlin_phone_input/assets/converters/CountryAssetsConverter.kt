package com.cleveroad.bootstrap.kotlin_phone_input.assets.converters

import com.cleveroad.bootstrap.kotlin_phone_input.data.converter.BaseConverter
import com.cleveroad.bootstrap.kotlin_phone_input.data.converter.Converter
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAsset
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAssetModel
import org.json.JSONObject

internal interface CountryAssetsConverter : Converter<JSONObject, CountryAsset>

internal class CountryAssetsConverterImpl : BaseConverter<JSONObject, CountryAsset>(), CountryAssetsConverter {

    companion object {
        private const val AB = "ab"
        private const val DIAL_CODE = "dial_code"
        private const val COUNTRY_ID = "country_id"
        private const val NAME = "name"
        private const val FORMAT = "format"
    }

    override fun processConvertInToOut(inObject: JSONObject) = inObject.run {
        CountryAssetModel(getString(AB), getLong(DIAL_CODE), getString(COUNTRY_ID), getString(NAME), getString(FORMAT))
    }

    override fun processConvertOutToIn(outObject: CountryAsset) = outObject.run {
        JSONObject(mapOf(AB to ab,
                DIAL_CODE to dialCode,
                COUNTRY_ID to countryId,
                NAME to name,
                FORMAT to phoneFormat))
    }
}