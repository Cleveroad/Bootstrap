package com.cleveroad.bootstrap.kotlin_phone_input.choose_country

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel
import com.cleveroad.bootstrap.kotlin_phone_input.Constants.PHONE_PREF
import com.cleveroad.bootstrap.kotlin_phone_input.assets.AssetsProviderImpl
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAsset

class ChooseCountryViewModel(application: Application) : BaseLifecycleViewModel(application) {
    var countriesLD = MutableLiveData<List<CountryAsset>>()

    var searchCountriesLD = MutableLiveData<List<CountryAsset>>()

    fun searchCountries(query: String) {
        val hasNumberOrPlus = hasNumberOrPlus(query)
        searchCountriesLD.value = countriesLD.value?.filter { countryAsset ->
            (if (hasNumberOrPlus) "$PHONE_PREF${countryAsset.dialCode}" else countryAsset.name)
                    .contains(query, true)
        }?.run countries@{
            if (hasNumberOrPlus) sortedBy { it.dialCode } else this@countries
        }
    }

    fun getCountriesFromAssets() {
        AssetsProviderImpl.getCountries(getApplication()).doAsync(countriesLD)
    }

    private fun hasNumberOrPlus(query: String) =
            query.contains("[0-9]".toRegex()) || query.contains(PHONE_PREF)
}