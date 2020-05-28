package com.cleveroad.bootstrap.kotlin_core.utils.misc

import com.cleveroad.bootstrap.kotlin_core.ui.NotImplementedInterfaceException

@Throws(AssertionError::class)
fun Boolean.assertTrue(message: String) =
        if (this) this else throw AssertionError(message)

@Throws(AssertionError::class)
fun Any?.assertNotEquals(anotherValue: Any, parameterName: String = "parameter") =
        (this == anotherValue).assertTrue("$parameterName can't be equal to $anotherValue.")

@Throws(AssertionError::class)
inline fun <reified T> Any?.assertInstanceOf(parameterName: String) =
        (this is T).assertTrue("$parameterName is not instance of ${T::class.java.name}.")

inline fun <reified T> bindInterfaceOrThrow(vararg objects: Any?): T =
        objects.find { it is T }
                ?.let { it as T }
                ?: throw NotImplementedInterfaceException(T::class.java)
