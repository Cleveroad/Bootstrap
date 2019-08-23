package com.cleveroad.bootstrap.kotlin_core.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cleveroad.bootstrap.kotlin_core.R
import com.cleveroad.bootstrap.kotlin_ext.hideKeyboard
import com.google.android.material.snackbar.Snackbar


/**
 * Base activity that implements work with the ViewModel and the life cycle.
 */
@Suppress("LeakingThis")
abstract class BaseLifecycleActivity<T : BaseLifecycleViewModel> : AppCompatActivity(),
        BaseView,
        BackPressedCallback,
        BottomNavigationCallback {

    /**
     * Set the Java-class ViewModel.
     */
    abstract val viewModelClass: Class<T>

    /**
     * Set id of the fragment container.
     */
    protected abstract val containerId: Int

    /**
     * Set id of layout.
     */
    protected abstract val layoutId: Int
    private var vProgress: View? = null
    protected abstract fun getProgressBarId(): Int
    protected open fun hasProgressBar(): Boolean = false
    protected abstract fun getSnackBarDuration(): Int

    protected open val viewModel: T by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(viewModelClass)
    }

    protected open val progressObserver = Observer<Boolean> { isShowProgress ->
        isShowProgress?.let { if (it) showProgress() else hideProgress() }
    }

    protected open val errorObserver = Observer<Any> { error ->
        error?.let { processError(it) }
    }

    private var showProgressCounter = 0

    /**
     * In the method need to subscribe to the LiveData from the [viewModel].
     */
    abstract fun observeLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        if (hasProgressBar()) vProgress = findViewById(getProgressBarId())
        observeAllLiveData()
    }

    override fun onBackPressed() {
        hideKeyboard()
        supportFragmentManager.findFragmentById(containerId)?.takeIf {
            it is BackPressable && it.onBackPressed()
        } ?: super.onBackPressed()
    }

    /**
     * Replace an existing fragment that was added to a container with animation.
     */
    protected open fun replaceFragmentAnimated(fragment: Fragment, needToAddToBackStack: Boolean = true,
                                               @AnimRes enter: Int = R.anim.enter_from_right,
                                               @AnimRes exit: Int = R.anim.exit_to_left) {
        hideKeyboard()
        val name = fragment.javaClass.simpleName
        with(supportFragmentManager.beginTransaction()) {
            setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            replace(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
    }

    /**
     * Replace an existing fragment that was added to a container.
     */
    protected open fun replaceFragment(fragment: Fragment, needToAddToBackStack: Boolean = true) {
        hideKeyboard()
        val name = fragment.javaClass.simpleName
        with(supportFragmentManager.beginTransaction()) {
            replace(containerId, fragment, name)
            if (needToAddToBackStack) {
                addToBackStack(name)
            }
            commit()
        }
    }

    /**
     * Remove fragment from the back stack by tag.
     */
    protected open fun removeFragmentByTag(tag: String) {
        with(supportFragmentManager) {
            supportFragmentManager.findFragmentByTag(tag)?.let {
                beginTransaction().remove(it).commitNowAllowingStateLoss()
            }
        }
    }

    /**
     * Removes all elements from this back stack.
     */
    protected open fun clearFragmentBackStack() {
        with(supportFragmentManager) {
            (backStackEntryCount - 1 downTo 0)
                    .forEach { popBackStack() }
        }
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     */
    protected open fun popBackStackFragment(clazz: Class<*>) =
            if (supportFragmentManager.findFragmentByTag(clazz.simpleName) != null) {
                supportFragmentManager.popBackStack(clazz.simpleName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                true
            } else {
                false
            }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    override fun backPressed() {
        with(supportFragmentManager) {
            backStackEntryCount.takeUnless { it == 0 }?.let { popBackStack() } ?: onBackPressed()
        }
    }

    /**
     * Checks if there is a fragment in the back stack of the fragment manager.
     * @param clazz Java-class of fragment
     */
    protected open fun checkFragmentInBackStack(clazz: Class<*>) =
            false.takeUnless {
                supportFragmentManager.findFragmentByTag(clazz.simpleName) == null
            } ?: true

    override fun showProgress() {
        ++showProgressCounter
        vProgress?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        --showProgressCounter
        if (showProgressCounter <= 0) {
            showProgressCounter = 0
            vProgress?.visibility = View.GONE
        }
    }

    /**
     * Show SnackBar with [StringRes]
     *
     * @param res Id of StringRes text to show
     */
    override fun showSnackBar(@StringRes res: Int) {
        showSnackBar(findViewById(android.R.id.content), res)
    }

    /**
     * Show SnackBar with [String]
     *
     * @param text String text to show
     */
    override fun showSnackBar(text: String?) {
        showSnackBar(findViewById(android.R.id.content), text)
    }

    /**
     * Show SnackBar with [StringRes]
     *
     * @param rootView Instance of [View]
     * @param res      Id of StringRes text to show
     */
    override fun showSnackBar(rootView: View?, @StringRes res: Int) {
        showSnackBar(rootView, getString(res))
    }

    /**
     * Show SnackBar with [String]
     *
     * @param rootView Instance of [View]
     * @param text     String text to show
     */
    override fun showSnackBar(rootView: View?, text: String?) {
        text?.let { txt ->
            rootView?.let {
                hideKeyboard()
                Snackbar.make(it, txt, getSnackBarDuration())
                        .apply {
                            view.setUpSnackBarView(this)
                            show()
                        }
            }
        }
    }

    override fun showSnackBar(text: Int, buttonText: Int, actionFun: () -> Unit, duration: Int) {
        Snackbar.make(findViewById(android.R.id.content), getString(text), duration)
                .apply {
                    this.setAction(buttonText) { actionFun() }
                    view.setUpSnackBarView(this)
                    show()
                }
    }

    protected open fun showAlert(message: String,
                                 title: String? = null,
                                 cancelable: Boolean = true,
                                 positiveRes: Int = R.string.ok,
                                 positiveFun: () -> Unit = {},
                                 negativeRes: Int? = R.string.no,
                                 negativeFun: () -> Unit = {}) {
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setCancelable(cancelable)
            title?.let { setTitle(it) }
            setPositiveButton(positiveRes) { _, _ -> positiveFun() }
            negativeRes?.let { setNegativeButton(it) { _, _ -> negativeFun() } }
            show()
        }
    }

    protected open fun showAlert(message: Int,
                                 title: Int? = null,
                                 cancelable: Boolean = true,
                                 positiveRes: Int = R.string.ok,
                                 positiveFun: () -> Unit = {},
                                 negativeRes: Int? = null,
                                 negativeFun: () -> Unit = {}) {
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setCancelable(cancelable)
            title?.let { setTitle(it) }
            setPositiveButton(positiveRes) { _, _ -> positiveFun() }
            negativeRes?.let { setNegativeButton(it) { _, _ -> negativeFun() } }
            show()
        }
    }

    /**
     * Error handler.
     */
    protected open fun processError(error: Any) = onError(error)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.findFragmentById(containerId)?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        supportFragmentManager.findFragmentById(containerId)?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun hasBottomNavigation(showBottomNavigation: Boolean) = Unit

    override fun onError(error: Any) {
        when (error) {
            is Throwable -> showSnackBar(error.message)
            is String -> showSnackBar(error)
            else -> showSnackBar(R.string.something_went_wrong)
        }
    }

    private fun observeAllLiveData() {
        observeLiveData()
        with(viewModel) {
            isLoadingLD.observe(this@BaseLifecycleActivity, progressObserver)
            errorLD.observe(this@BaseLifecycleActivity, errorObserver)
        }
    }

    private fun View.setUpSnackBarView(snackBar: Snackbar) = with(this) {
        setOnClickListener { snackBar.dismiss() }
        with(findViewById<TextView>(com.google.android.material.R.id.snackbar_text)) {
            maxLines = BASE_SNACK_BAR_MAX_LINES
        }
    }
}