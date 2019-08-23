package com.cleveroad.auth_example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleActivity
import com.cleveroad.bootstrap.kotlin_core.ui.BlockedCallback
import com.google.android.material.snackbar.Snackbar

class SampleAuthActivity : BaseLifecycleActivity<SampleAuthVM>(),
        BlockedCallback {

    companion object {
        fun start(context: Context) =
                context.startActivity(Intent(context, SampleAuthActivity::class.java))
    }

    override val viewModelClass = SampleAuthVM::class.java

    override val containerId = R.id.flContainer

    override val layoutId = R.layout.activity_auth

    override fun getProgressBarId() = R.id.progressBar

    override fun getSnackBarDuration() = Snackbar.LENGTH_SHORT

    override fun observeLiveData() = Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(AuthFragment.newInstance())
    }

    override fun onBlocked() = Unit
}