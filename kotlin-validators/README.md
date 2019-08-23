# Kotlin Validators [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>

## Meet Kotlin Validators by Cleveroad

The library contains a set of base classes for validation.

### Description ###
#### Interfaces: ####

- Validator - Interface for field Validation class
- PhoneValidator - Interface for advanced phone validation
- MatchValidator - Interface for validation two fields with matching
- DateValidator - Interface for validation
- ValidationResponse - Interface for encapsulation information about results of validation
- MatchValidationResponse - Interface for encapsulating result of validation of more then 1 fields

#### Classes: ####

- NameValidator
- EmailValidator
- PasswordValidator
- MatchPasswordValidator
- MatchValidationResponseImpl
- PhoneValidatorImpl
- DateValidatorImpl
- ValidationResponseImpl

## Setup and usage
### Installation
by gradle : 
```groovy
dependencies {
    implementation 'com.cleveroad.bootstrap:kotlin-validators:2.0.0'
}
```

### Usage ###
```groovy
class ValidatorActivity : AppCompatActivity() {

    private var nameValidator: Validator? = null
    private var emailValidator: Validator? = null
    private var nameWatcher: SimpleTextWatcher? = null
    private var emailWatcher: SimpleTextWatcher? = null

    companion object {
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
                emailValidator?.validate(etEmail.text.toString())
                        ?.let { showTextInputLayoutError(tilEmail, it) }
            }
        }

        bValidateName.setOnClickListener {
            nameValidator?.validate(etName.text.toString())?.let { showValidateResponse(it) }
        }
        bValidateEmail.setOnClickListener {
            emailValidator?.validate(etEmail.text.toString())?.let { showValidateResponse(it) }
        }
    }

    private fun showTextInputLayoutError(til: TextInputLayout, validate: ValidationResponse) {
        til.error = if (validate.isValid) "" else validate.errorMessage ?: getString(R.string.error)
    }

    private fun hideError(til: TextInputLayout) {
        til.error = ""
    }

    override fun onStop() {
        etName.removeTextChangedListener(nameWatcher)
        etEmail.removeTextChangedListener(emailWatcher)
        etEmail.onFocusChangeListener = null
        super.onStop()
    }

    private fun initValidators() {
        nameValidator = ValidatorsFactory.getNameValidator(this)
        emailValidator = EmailValidator.Builder(this)
                .emptyError("Email is empty")
                .invalidError("Email is invalid")
                .build()
    }

    private fun showValidateResponse(validate: ValidationResponse) {
        if (validate.isValid) showMessage() else showMessage(validate.errorMessage
                ?: getString(R.string.error))
    }

    private fun showMessage(message: String = getString(R.string.correct)) =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
}
```

### Support ###
If you have any questions, issues or propositions, please create a <a href="../../issues/new">new issue</a> in this repository.

If you want to hire us, send an email to sales@cleveroad.com or fill the form on <a href="https://www.cleveroad.com/contact">contact page</a>

Follow us:

[![Awesome](/images/social/facebook.png)](https://www.facebook.com/cleveroadinc/)   [![Awesome](/images/social/twitter.png)](https://twitter.com/cleveroadinc)   [![Awesome](/images/social/google.png)](https://plus.google.com/+CleveroadInc)   [![Awesome](/images/social/linkedin.png)](https://www.linkedin.com/company/cleveroad-inc-)   [![Awesome](/images/social/youtube.png)](https://www.youtube.com/channel/UCFNHnq1sEtLiy0YCRHG2Vaw)
<br/>

### License ###
* * *
    The MIT License (MIT)
    
    Copyright (c) 2016 Cleveroad Inc.
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
