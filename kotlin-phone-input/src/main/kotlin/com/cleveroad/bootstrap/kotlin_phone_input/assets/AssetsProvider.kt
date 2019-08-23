package com.cleveroad.bootstrap.kotlin_phone_input.assets

import android.content.Context
import com.cleveroad.bootstrap.kotlin_phone_input.assets.converters.CountryAssetsConverter
import com.cleveroad.bootstrap.kotlin_phone_input.assets.converters.CountryAssetsConverterImpl
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAsset
import io.reactivex.Single

internal interface AssetsProvider {
    fun getCountries(context: Context): Single<List<CountryAsset>>
}

internal object AssetsProviderImpl : AssetsProvider {
    private val countryAssetsConverter: CountryAssetsConverter by lazy { CountryAssetsConverterImpl() }

    override fun getCountries(context: Context): Single<List<CountryAsset>> =
            AssetsModuleImpl.getCountries(context).compose(countryAssetsConverter.jsonArraySingleINtoOUT())
}