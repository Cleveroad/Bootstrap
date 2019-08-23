package com.cleveroad.bootstrap.kotlin_validators

/**
 * Interface for encapsulating result of validation of more then 1 fields
 */
interface MatchValidationResponse : ValidationResponse {

    /**
     * Returns number of wrong field
     *
     * @return 0 if validation was successful and number of field failed validation in another case
     */
    val invalidFieldNumber: Int
}
