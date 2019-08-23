package com.cleveroad.bootstrap.kotlin_validators

/**
 * Interface for encapsulation information about results of validation
 */
interface ValidationResponse {

    /**
     * Return information about validation result
     *
     * @return true if validation was successful and false in another case
     */
    val isValid: Boolean

    /**
     * Returns message with information about cause of validation error
     * @return Message about validation error or null if validation was successful
     */
    val errorMessage: String?
}
