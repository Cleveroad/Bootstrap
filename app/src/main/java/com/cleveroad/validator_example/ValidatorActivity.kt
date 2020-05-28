package com.cleveroad.validator_example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_validators.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_validator.*


class ValidatorActivity : AppCompatActivity() {

    private var nameValidator: Validator? = null
    private var emailValidator: Validator? = null
    private var phoneValidator: PhoneValidator? = null
    private var passwordValidator: Validator? = null
    private var matchPasswordValidator: MatchPasswordValidator? = null
    private var nameWatcher: SimpleTextWatcher? = null
    private var emailWatcher: SimpleTextWatcher? = null
    private var countryRegion = ""

    companion object {
        private const val EMPTY_MESSAGE = ""

        fun start(context: Context) = context.startActivity(Intent(context, ValidatorActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validator)
        initValidators()

        nameWatcher = etName.addSimpleTextChangedListener { text ->
            nameValidator?.validate(text)?.let { showTextInputLayoutError(tilName, it) }
        }

        emailWatcher = etEmail.addSimpleTextChangedListener { _ -> hideError(tilEmail) }

        etEmail.addFocusChangedListener { hasFocus ->
            if (!hasFocus) {
                emailValidator?.validate(etEmail.getStringText())
                        ?.let { showTextInputLayoutError(tilEmail, it) }
            }
        }

        bValidateName.setOnClickListener {
            nameValidator?.validate(etName.getStringText())?.let { showValidateResponse(it) }
        }
        bValidateEmail.setOnClickListener {
            emailValidator?.validate(etEmail.getStringText())?.let { showValidateResponse(it) }
        }
        bValidatePhone.setOnClickListener {
            phoneValidator?.validate(etPhone.getStringText(), countryRegion)
                    ?.let { showValidateResponse(it) }
        }
        bValidatePassword.setOnClickListener {
            passwordValidator?.validate(etPassword.getStringText())?.let { showValidateResponse(it) }
        }
        bValidateMatchPassword.setOnClickListener {
            matchPasswordValidator?.validate(etFirstPassword.getStringText(), etSecondPassword.getStringText())
                    ?.let { showValidateResponse(it) }
        }
    }

    private fun showTextInputLayoutError(til: TextInputLayout, validate: ValidationResponse) {
        til.error = if (validate.isValid) EMPTY_MESSAGE else validate.errorMessage
                ?: getString(R.string.error)
    }

    private fun hideError(til: TextInputLayout) {
        til.error = EMPTY_MESSAGE
    }

    override fun onStop() {
        etName.removeTextChangedListener(nameWatcher)
        etEmail.removeTextChangedListener(emailWatcher)
        etEmail.onFocusChangeListener = null
        super.onStop()
    }

    private fun initValidators() {
        nameValidator = ValidatorsFactory.getNameValidator(this)
        emailValidator = ValidatorsFactory.getEmailValidator(this)
        phoneValidator = ValidatorsFactory.getPhoneValidator(this)
        passwordValidator = ValidatorsFactory.getPasswordValidator(this)
        matchPasswordValidator = ValidatorsFactory.getMatchPasswordValidator(this)
    }

    private fun showValidateResponse(validate: ValidationResponse) {
        if (validate.isValid) showMessage() else showMessage(validate.errorMessage
                ?: getString(R.string.error))
    }

    private fun showMessage(message: String = getString(R.string.correct)) =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
}
