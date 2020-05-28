package com.cleveroad.bootstrap.kotlin_validators

/**
 * Interface for advanced phone validation
 */
interface PhoneValidator : Validator {

    /**
     * Checks phone for valid value
     *
     * @param phone         Phone for validation
     * @param countryRegion Code for country region (for example "US" for United States
     * or "UA" for Ukraine)
     * @return Instance of [ValidationResponse]
     */
    fun validate(phone: String?, countryRegion: String?): ValidationResponse
}
