package com.cleveroad.bootstrap.kotlin_validators


import java.util.*

/**
 * Interface for validation
 */
interface DateValidator {

    /**
     * Checks date for valid value
     *
     * @param date Date for validation
     * @return Instance of [ValidationResponse]
     */
    fun validate(date: Date?): ValidationResponse

    /**
     * Checks date for valid value
     *
     * @param year        Year
     * @param monthOfYear Month
     * @param dayOfMonth  Day
     * @return Instance of [ValidationResponse]
     */
    fun validate(year: Int, monthOfYear: Int, dayOfMonth: Int): ValidationResponse
}
