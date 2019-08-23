package com.cleveroad.bootstrap.kotlin_ext

/**
 * throw AssertionError if parameter is null
 */
@Throws(AssertionError::class)
fun Any?.assertNotNull(parameterName: String = "") = this
        ?: throw AssertionError("$parameterName can't be null.")

/**
 * throw AssertionError if boolean value is not true
 */
@Throws(AssertionError::class)
fun Boolean.assertTrue(message: String) = if (this) this else throw AssertionError(message)

/**
 * throw AssertionError if boolean value is not false
 */
@Throws(AssertionError::class)
fun Boolean.assertFalse(message: String) = this.not().assertTrue(message)

/**
 * throw AssertionError if value is not equals another value
 */
@Throws(AssertionError::class)
fun Any?.assertNotEquals(anotherValue: Any, parameterName: String = "parameter") =
        (this == anotherValue).assertTrue("$parameterName can't be equal to $anotherValue.")

/**
 * throw AssertionError if string is null or empty
 */
@Throws(AssertionError::class)
fun String?.assertNotEmpty(parameterName: String) =
        this.isNullOrBlank().assertFalse("$parameterName can't be empty.")

/**
 * throw AssertionError is value is not belong to class T
 */
@Throws(AssertionError::class)
inline fun <reified T> Any?.assertInstanceOf(parameterName: String) =
        (this is T).assertTrue(parameterName + " is not instance of " + T::class.java.name + ".")
