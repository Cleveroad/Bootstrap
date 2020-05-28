package com.cleveroad.bootstrap.kotlin_validators

/**
 * Interface for validation two fields with matching
 */
interface MatchValidator {

    /**
     * Validates two fields and check them for match
     *
     * @param value1 First value for for validation
     * @param value2 Second value for for validation
     * @return Instance of [ValidationResponse]
     */
    fun validate(value1: String?, value2: String?): MatchValidationResponse
}
