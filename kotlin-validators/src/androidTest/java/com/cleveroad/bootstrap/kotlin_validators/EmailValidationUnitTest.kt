package com.cleveroad.bootstrap.kotlin_validators

import android.content.Context
import android.text.TextUtils
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class EmailValidationUnitTest {
    private var emailValidator: Validator? = null
    private var context: Context? = null
    private val validEmails = mutableListOf<String>()
    private val wrongEmails = mutableListOf<String>()

    @Before
    fun setup() {
        context = InstrumentationRegistry.getTargetContext()
        context?.let {
            emailValidator = EmailValidator.getDefaultValidator(it)
            it.resources?.getStringArray(R.array.valid_emails)?.let {
                validEmails.addAll(it)
            }
            it.resources?.getStringArray(R.array.invalid_emails)?.let {
                wrongEmails.addAll(it)
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun rightEmail() {
        assertTrue(emailValidator != null)
        emailValidator?.let {
            for (email in validEmails) {
                val response = it.validate(email)
                assertTrue(response.isValid)
                assertTrue(TextUtils.isEmpty(response.errorMessage))
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun wrongEmail() {
        assertTrue(emailValidator != null)
        emailValidator?.let {
            for (wrongEmail in wrongEmails) {
                val response = it.validate(wrongEmail)
                assertTrue(!response.isValid)
                assertTrue(!TextUtils.isEmpty(response.errorMessage))
            }
        }
    }
}
