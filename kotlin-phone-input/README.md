# Kotlin PhoneInput [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>

## Meet Kotlin PhoneInput by Cleveroad

Used to implement a field for entering a phone number/

### Description ###
The library implies an implementation of Custom ViewGroup with the help of which it is easy and quick to make an implementation of a field for entering a phone number. The library has support for determining the country code. Phone numbers are automatically formatted according to the selected country.

## Setup and usage
### Installation
by gradle : 
```groovy
dependencies {
    implementation 'com.cleveroad.bootstrap:kotlin-phone-input:3.0.0'
}
```

### Usage ###

```xml
<com.cleveroad.bootstrap.kotlin_phone_input.view.PhoneInputLayout
        android:id="@+id/pvCodePicker"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/PhoneTheme"
        attrs:pcv_flag_shape="circle"
        attrs:pcv_icon_size="30dp"
        attrs:pcv_icon_position="start">

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/tilCode"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginEnd="20dp">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/etCode"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />

        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/tilPhone"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            attrs:hintEnabled="false"
            attrs:errorEnabled="true">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/etPhone"
                android:layout_width="match_parent"
                android:layout_height="wrap_content" />

        </com.google.android.material.textfield.TextInputLayout>
</com.cleveroad.bootstrap.kotlin_phone_input.view.PhoneInputLayout>
```

|  attribute name | description |
|---|---|
| pcv_icon_size  | Value for size of displaying icon. |
| pcv_icon_position  | Value for position of displaying icon. |
| pcv_flag_shape  | Enum class with its help can set the shape of the icon. |
| pcv_custom_flag_path  | Use the attribute to set the custom shape for the icon. |

```kotlin
class PhoneActivity : BaseLifecycleActivity<PhoneVM>(), PhoneViewCallback {

    ...

    override fun showChooseCountry() {
        supportFragmentManager.fragments.firstOrNull()?.let {
            replaceFragment(ChooseCountryFragment.newInstance(it, RequestCode.REQUEST_CHOOSE_COUNTRY()))
        }
    }
}


 class PhoneViewVM(application: Application) : BaseLifecycleViewModel(application) {
 
     private val phoneValidator = PhoneValidatorImpl
                                              .builder(application)
                                              .emptyErrorMessage(getString(R.string.phone_is_invalid))
                                              .invalidErrorMessage(getString(R.string.phone_is_invalid))
                                              .build()
 
     val countryLD = MutableLiveData<CountryAsset>()
     val validationLD = MutableLiveData<ValidationResponse>()
 
     fun validatePhoneNumber(phoneNumber: String, countryRegion: String): Boolean =
             phoneValidator.validate(phoneNumber, countryRegion).run {
                 validationLD.value = this
                 isValid
             }
 }


 class PhoneViewFragment : BaseLifecycleFragment<PhoneViewVM>() {
 
     override val viewModelClass = PhoneViewVM::class.java
     private var phoneViewCallback: PhoneViewCallback? = null
 
     override fun observeLiveData() {
         with(viewModel) {
             countryLD.observe(this@PhoneViewFragment, Observer { countryAsset ->
                 countryAsset?.let { pvCodePicker?.setData(it) }
             })
             validationLD.observe(this@PhoneViewFragment, Observer { response ->
                 response?.let {
                     tilPhone.error = it.errorMessage.takeIf { _ -> !it.isValid }
                 }
             })
         }
     }
 
     override fun onAttach(context: Context) {
         super.onAttach(context)
         phoneViewCallback = bindInterfaceOrThrow<PhoneViewCallback>(context)
     }
 
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         data?.takeIf { resultCode == Activity.RESULT_OK && requestCode == RequestCode.REQUEST_CHOOSE_COUNTRY() }?.let {
             it.getParcelableExtra<CountryAsset>(ChooseCountryFragment.CHOOSE_COUNTRY_EXTRA)?.let { countryAsset ->
                 viewModel.countryLD.value = countryAsset
             }
         }
     }
 
     override fun onDetach() {
         phoneViewCallback = null
         super.onDetach()
     }
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
