package com.cleveroad.phone_example

import android.content.Context
import android.view.View
import com.cleveroad.bootstrap.kotlin.BuildConfig
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleFragment
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel


abstract class BaseFragment<T : BaseLifecycleViewModel> : BaseLifecycleFragment<T>() {

    override var endpoint = ""

    override var versionName = ""

    override fun getVersionsLayoutId() = View.NO_ID

    override fun getEndPointTextViewId() = View.NO_ID

    override fun getVersionsTextViewId() = View.NO_ID

    override fun isDebug() = BuildConfig.DEBUG

    override fun showBlockBackAlert() = Unit

}