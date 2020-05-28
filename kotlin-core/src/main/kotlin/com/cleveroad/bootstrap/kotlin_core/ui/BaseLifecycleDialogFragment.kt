package com.cleveroad.bootstrap.kotlin_core.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cleveroad.bootstrap.kotlin_core.utils.misc.bindInterfaceOrThrow
import com.cleveroad.bootstrap.kotlin_ext.hideKeyboard

/**
 * Base fragment for the implementation of the dialog with custom view.
 */
abstract class BaseLifecycleDialogFragment<T : BaseLifecycleViewModel> : BaseDialogFragment(),
        BaseView {

    /**
     * Set the Java-class ViewModel.
     */
    abstract val viewModelClass: Class<T>

    protected open val viewModel: T by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(viewModelClass)
    }

    private var baseView: BaseView? = null

    /**
     * Handler to show or hide the progress view.
     */
    protected open val progressObserver = Observer<Boolean> {
        if (it == true) showProgress() else hideProgress()
    }

    /**
     * Error handler.
     */
    protected open val errorObserver = Observer<Any> { error ->
        error?.let { onError(it) }
    }

    /**
     * In the method need to subscribe to the LiveData from the [viewModel].
     */
    abstract fun observeLiveData()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseView = bindInterfaceOrThrow<BaseView>(parentFragment, context)
    }

    override fun onDetach() {
        baseView = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAllLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutId, container, false).also { hideKeyboard(it) }

    override fun onResume() {
        super.onResume()
        view?.let { hideKeyboard(it) }
    }

    /**
     * Called when need to show progress view
     */
    override fun showProgress() {
        baseView?.showProgress()
    }

    /**
     * Called when need to hide progress view
     */
    override fun hideProgress() {
        baseView?.hideProgress()
    }

    /**
     * Show SnackBar with [StringRes]
     *
     * @param res Id of StringRes text to show
     */
    override fun showSnackBar(@StringRes res: Int) {
        baseView?.showSnackBar(view, res)
    }

    /**
     * Show SnackBar with [String]
     *
     * @param text String text to show
     */
    override fun showSnackBar(text: String?) {
        baseView?.showSnackBar(view, text)
    }

    /**
     * Show SnackBar with [StringRes]
     *
     * @param res Id of [StringRes] to show
     * @param rootView Instance of [View]
     */
    override fun showSnackBar(rootView: View?, @StringRes res: Int) {
        baseView?.showSnackBar(rootView, getString(res))
    }

    /**
     * Show SnackBar with [String]
     *
     * @param text [String] text to show
     * @param rootView Instance of [View]
     */
    override fun showSnackBar(rootView: View?, text: String?) {
        baseView?.showSnackBar(rootView, text)
    }

    /**
     * Show SnackBar with [StringRes] and function for execution
     *
     * @param actionFun Function for execution
     * @param text       Id of StringRes text to show
     */
    override fun showSnackBar(@StringRes text: Int, @StringRes buttonText: Int, actionFun: () -> Unit, duration: Int) {
        baseView?.showSnackBar(text, buttonText, actionFun, duration)
    }

    /**
     * Error handler
     *
     * @param error object [Any] which describe error
     */
    override fun onError(error: Any) {
        baseView?.onError(error)
    }

    private fun observeAllLiveData() {
        observeLiveData()
        with(viewModel) {
            isLoadingLD.observe(this@BaseLifecycleDialogFragment, progressObserver)
            errorLD.observe(this@BaseLifecycleDialogFragment, errorObserver)
        }
    }
}