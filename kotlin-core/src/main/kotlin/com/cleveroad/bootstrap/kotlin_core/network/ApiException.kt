package com.cleveroad.bootstrap.kotlin_core.network

/**
 * Error from server.
 */
open class ApiException : Exception {

    open var statusCode: Int? = null
    var showMessage: String? = null
        get() = field.takeUnless { it.isNullOrEmpty() } ?: super.message

    var v: String? = null
    var errors: List<ValidationError>? = null
    var stacktrace: String? = null

    constructor() : super()

    constructor(statusCode: Int?,
                v: String?,
                message: String?,
                errors: List<ValidationError>?,
                stacktrace: String? = null) : super(message) {
        this.statusCode = statusCode
        this.showMessage = message
        this.v = v
        this.errors = errors
        this.stacktrace = stacktrace
    }

    override val message
        get() = showMessage
}

data class ValidationError(var code: Int?, var key: String?, var message: String?)