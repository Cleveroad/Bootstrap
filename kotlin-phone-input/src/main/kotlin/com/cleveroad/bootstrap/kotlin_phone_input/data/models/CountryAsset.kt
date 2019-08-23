package com.cleveroad.bootstrap.kotlin_phone_input.data.models

import android.os.Parcel
import android.os.Parcelable

interface CountryAsset : Parcelable {
    var ab: String
    var dialCode: Long
    var countryId: String
    var name: String
    var phoneFormat: String
}

data class CountryAssetModel(override var ab: String,
                             override var dialCode: Long,
                             override var countryId: String,
                             override var name: String,
                             override var phoneFormat: String) : KParcelable, CountryAsset {

    companion object {
        @JvmField
        val CREATOR = KParcelable.generateCreator {
            CountryAssetModel(it.read(), it.read(), it.read(), it.read(), it.read())
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) =
            dest.write(ab, dialCode, countryId, name, phoneFormat)
}