package com.cleveroad.bootstrap.kotlin_validators

class MatchValidationResponseImpl
internal constructor(isValid: Boolean, errorMessage: String?, override val invalidFieldNumber: Int) : ValidationResponseImpl(isValid, errorMessage), MatchValidationResponse {

    internal constructor(validationResponse: ValidationResponse, fieldNumber: Int) : this(validationResponse.isValid, validationResponse.errorMessage, fieldNumber) {}
}
