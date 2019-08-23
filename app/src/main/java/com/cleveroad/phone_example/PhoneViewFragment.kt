package com.cleveroad.phone_example

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.NO_TOOLBAR
import com.cleveroad.bootstrap.kotlin_core.utils.misc.bindInterfaceOrThrow
import com.cleveroad.bootstrap.kotlin_ext.clickWithDebounce
import com.cleveroad.bootstrap.kotlin_phone_input.choose_country.ChooseCountryFragment
import com.cleveroad.bootstrap.kotlin_phone_input.data.models.CountryAsset
import kotlinx.android.synthetic.main.fragment_phone_view.*

class PhoneViewFragment : BaseFragment<PhoneViewVM>() {

    companion object {
        fun newInstance() = PhoneViewFragment().apply {
            arguments = Bundle()
        }
    }

    private var flagViewVisibility: Boolean = true

    private var phoneViewCallback: PhoneViewCallback? = null

    override val viewModelClass = PhoneViewVM::class.java

    override fun hasToolbar() = false
    override fun hasVersions() = true
    override fun getToolbarId() = NO_TOOLBAR

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

    override fun getScreenTitle() = R.string.country

    override val layoutId = R.layout.fragment_phone_view

    override fun onAttach(context: Context) {
        super.onAttach(context)
        phoneViewCallback = bindInterfaceOrThrow<PhoneViewCallback>(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTestButtons()
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

    private fun initTestButtons() {
        tilPhone.editText?.addTextWatcher(BaseTextWatcher(actionOnTextChanged = { _, _, _, _ ->
            tilPhone.error = null
        }))

        tilCode.editText?.clickWithDebounce {
            phoneViewCallback?.showChooseCountry()
        }

        with(pvCodePicker) {
            btFlagVisible.setOnClickListener {
                flagViewVisibility = !flagViewVisibility
                flagImageVisibility(flagViewVisibility)
            }
            btValidation.setOnClickListener {
                val (code, phone) = getPhone()
                viewModel.validatePhoneNumber(phone, code)
            }
            btIcon24.setOnClickListener { setIconSize(60) }
            btIcon36.setOnClickListener { setIconSize(80) }
            btIcon48.setOnClickListener { setIconSize(100) }
            btIcon60.setOnClickListener { setIconSize(120) }
        }
    }

}