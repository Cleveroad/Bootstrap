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
import com.cleveroad.bootstrap.kotlin_phone_input.utils.IconPosition
import kotlinx.android.synthetic.main.fragment_phone_view.*

class PhoneViewFragment : BaseFragment<PhoneViewVM>() {

    companion object {
        private const val ICON_SIZE_SMALL = 60
        private const val ICON_SIZE_MEDIUM = 80
        private const val ICON_SIZE_LARGE = 100
        private const val ICON_SIZE_X_LARGE = 120

        fun newInstance() = PhoneViewFragment().apply {
            arguments = Bundle()
        }
    }

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
                    validationLD.value = null
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
            bFlagVisible.setOnClickListener {
                setFlagImageVisibility(getFlagImageVisibility().not())
            }
            bValidation.setOnClickListener {
                val (code, phone) = getPhone()
                val countryCode = getCountryCode()
                viewModel.validatePhoneNumber(code.plus(phone), countryCode)
            }
            bFlagPosition.setOnClickListener {
                setIconPosition(IconPosition.getPosition(getIconPosition().invoke().inc()))
            }
            bIcon24.setOnClickListener { setIconSize(ICON_SIZE_SMALL) }
            bIcon36.setOnClickListener { setIconSize(ICON_SIZE_MEDIUM) }
            bIcon48.setOnClickListener { setIconSize(ICON_SIZE_LARGE) }
            bIcon60.setOnClickListener { setIconSize(ICON_SIZE_X_LARGE) }
        }
    }
}
