package com.cleveroad.bootstrap.kotlin_validators


import android.content.Context
import androidx.annotation.StringRes
import java.util.*

open class DateValidatorImpl private constructor(private val invalidError: String,
                                                 private val notBeforeDate: Date,
                                                 private val isCheckSameDay: Boolean) : DateValidator {

    companion object {
        inline fun build(context: Context, block: DateValidatorImpl.Builder.() -> Unit) = DateValidatorImpl.Builder(context).apply(block).build()

        fun builder(context: Context) = EmailValidator.Builder(context)

        fun getDefaultValidator(context: Context) = builder(context).build()
    }

    override fun validate(date: Date?): ValidationResponse {
        val currentDate = Date()

        val error = if (date == null || date.before(notBeforeDate) || date.after(currentDate)
                || isCheckSameDay && isSameDay(date, currentDate)) {
            invalidError
        } else {
            ""
        }
        return ValidationResponseImpl(error.isEmpty(), error)
    }

    override fun validate(year: Int, monthOfYear: Int, dayOfMonth: Int): ValidationResponse {
        var error = ""
        GregorianCalendar(year, monthOfYear, dayOfMonth)
                .apply {
                    if (after(Calendar.getInstance())) {
                        error = invalidError
                    }
                }
        return ValidationResponseImpl(error.isEmpty(), error)
    }

    private fun isSameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) {
            return false
        }
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    class Builder(aContext: Context) {
        private val context = aContext.applicationContext
        private var invalidError = context.getString(R.string.date_is_not_valid)
        private var notBeforeDate: Date = GregorianCalendar(1900, 0, 1).time
        private var isCheckSameDay = true

        fun invalidError(invalidError: String?) = apply { this.invalidError = invalidError ?: "" }

        fun invalidError(@StringRes invalidError: Int?) = apply {
            this.invalidError = invalidError?.let { context.getString(it) } ?: ""
        }

        fun notBeforeDate(date: Date) = apply { notBeforeDate = date }

        fun isCheckSameDay(isCheckSameDay: Boolean?) = apply {
            this.isCheckSameDay = isCheckSameDay ?: false
        }

        fun build() = DateValidatorImpl(invalidError, notBeforeDate, isCheckSameDay)
    }
}

