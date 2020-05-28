package com.cleveroad.bootstrap.kotlin_phone_input.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface CountryAsset : Parcelable {
    var ab: String
    var dialCode: Long
    var countryId: String
    var name: String
    var phoneFormat: String
}

@Parcelize
data class CountryAssetModel(override var ab: String,
                             override var dialCode: Long,
                             override var countryId: String,
                             override var name: String,
                             override var phoneFormat: String) : CountryAsset