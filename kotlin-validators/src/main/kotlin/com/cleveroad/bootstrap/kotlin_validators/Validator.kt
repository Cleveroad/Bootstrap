package com.cleveroad.bootstrap.kotlin_validators

/**
 * Interface for field Validation class
 */
interface Validator {

    /**
     * Validates field
     *
     * @param value Value for for validation
     * @return Instance of [ValidationResponse]
     */
    fun validate(value: String?): ValidationResponse
}
