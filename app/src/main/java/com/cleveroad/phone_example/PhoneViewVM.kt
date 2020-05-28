package com.cleveroad.phone_example

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAsset
import com.cleveroad.bootstrap.kotlin_phone_input.utils.IconPosition
import com.cleveroad.bootstrap.kotlin_validators.ValidationResponse

class PhoneViewVM(application: Application) : BaseLifecycleViewModel(application) {

    private val phoneValidator = getPhoneValidator(application)

    val countryLD = MutableLiveData<CountryAsset>()
    val validationLD = MutableLiveData<ValidationResponse>()

    fun validatePhoneNumber(phoneNumber: String, countryRegion: String): Boolean =
            phoneValidator.validate(phoneNumber, countryRegion).run {
                validationLD.value = this
                isValid
            }
}
