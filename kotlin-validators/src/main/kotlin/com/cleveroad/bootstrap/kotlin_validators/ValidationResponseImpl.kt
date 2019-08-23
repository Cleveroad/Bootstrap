package com.cleveroad.bootstrap.kotlin_validators


open class ValidationResponseImpl(override val isValid: Boolean,
                                  override val errorMessage: String?) : ValidationResponse
