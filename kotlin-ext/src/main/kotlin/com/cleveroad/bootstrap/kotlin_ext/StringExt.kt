package com.cleveroad.bootstrap.kotlin_ext

import android.net.Uri
import android.util.Base64

/**
 * Creates a Uri which parses the given encoded URI string
 */
fun String.toUri() = Uri.parse(this)

/**
 * @return true if contains a digit.
 */
fun String.containsDigit() = any { it.isDigit() }

/**
 * @return true if string matches the "(?=.*?[^a-zA-Z\\s]).+\$" regular expression.
 */
fun String.containsSymbolOrNumber() = matches("(?=.*?[^a-zA-Z\\s]).+\$".toRegex())

/**
 * @return true if contains character in upper case
 */
fun String.containsUpperCase() = any { it.isUpperCase() }

/**
 * @return true if contains character in lower case
 */
fun String.containsLowerCase() = any { it.isLowerCase() }

/**
 * Decode a string from Base64 to ByteArray
 *
 * @return ByteArray
 */
fun String?.fromBase64(): ByteArray? = this?.let { Base64.decode(it, Base64.NO_WRAP) }

/**
 * Encode a string to Base64
 *
 * @return String
 */
fun String?.toBase64FromString(): String? = this?.let { String(Base64.encode(it.toByteArray(), Base64.NO_WRAP)) }

/**
 * Decode a string from Base64 to String
 *
 * @return String
 */
fun String?.fromBase64ToString(): String? = this?.let { String(Base64.decode(it, Base64.NO_WRAP)) }
