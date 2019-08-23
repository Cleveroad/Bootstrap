package com.cleveroad.phone_example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleActivity
import com.cleveroad.bootstrap.kotlin_core.ui.BlockedCallback
import com.cleveroad.bootstrap.kotlin_core.ui.NO_ID
import com.cleveroad.bootstrap.kotlin_phone_input.choose_country.ChooseCountryFragment

class ChooseCountryActivity : BaseLifecycleActivity<ChooseCountryVM>(), PhoneViewCallback, BlockedCallback {

    companion object {
        fun start(context: Context) = context.startActivity(Intent(context, ChooseCountryActivity::class.java))
    }

    override val containerId = R.id.flContainer

    override val layoutId = R.layout.activity_choose_country

    override val viewModelClass = ChooseCountryVM::class.java

    override fun onBlocked() = Unit

    override fun getProgressBarId() = NO_ID

    override fun getSnackBarDuration() = 1000

    override fun observeLiveData() = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(PhoneViewFragment.newInstance(), false)
    }

    override fun showChooseCountry() {
        supportFragmentManager.fragments.firstOrNull()?.let {
            replaceFragment(ChooseCountryFragment.newInstance(it, RequestCode.REQUEST_CHOOSE_COUNTRY()))
        }
    }

}