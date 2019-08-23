package com.cleveroad.bootstrap.kotlin_core.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_core.network.ApiException
import com.cleveroad.bootstrap.kotlin_ext.doAsync
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


/**
 * BaseViewModel is a class that is responsible for preparing and managing the data for an Activity or a Fragment.
 */
abstract class BaseLifecycleViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val EMPTY_ERROR = ""
        private const val STRING_SPACE_VALUE = " "
    }

    val errorLD = MutableLiveData<Any>()
    val isLoadingLD = MediatorLiveData<Boolean>()
    val blockedUserLD = MutableLiveData<Boolean>()

    protected open val onErrorConsumer = Consumer<Throwable> {
        val errorString = parseApiException(it)
        errorLD.value = if (errorString.isNotEmpty()) errorString else it.message
    }

    protected var compositeDisposable: CompositeDisposable? = null

    override fun onCleared() {
        clearSubscription()
        super.onCleared()
    }

    @Suppress("unused")
    protected fun hideProgress() {
        isLoadingLD.value = false
    }

    @Suppress("unused")
    protected fun showProgress() {
        isLoadingLD.value = true
    }

    @Suppress("unused")
    protected fun hideOrShowProgress(hideOrShowFlag: Boolean) {
        isLoadingLD.value = hideOrShowFlag
    }

    /**
     * Add [Disposable] to [CompositeDisposable] to clean up data when ViewModel collapses.
     */
    protected open fun Disposable.addSubscription() = addBackgroundSubscription(this)

    protected fun handleError(block: () -> Unit, statusCode: Int) = Consumer<Throwable> {
        if (it is ApiException && it.statusCode == statusCode) block() else onErrorConsumer.accept(it)
    }

    /**
     * Helper methods for working with RxJava
     */
    protected fun <T> Flowable<T>.doAsync(successful: Consumer<T>,
                                          error: Consumer<Throwable> = onErrorConsumer,
                                          isShowProgress: Boolean = true) {
        doAsync(successful, error, isLoadingLD, isShowProgress)
                .addSubscription()
    }

    protected fun <T> Flowable<T>.doAsync(successful: MutableLiveData<T>,
                                          error: Consumer<Throwable> = onErrorConsumer,
                                          isShowProgress: Boolean = true) {
        doAsync(successful, error, isLoadingLD, isShowProgress)
                .addSubscription()
    }

    protected fun <T> Single<T>.doAsync(successful: Consumer<T>,
                                        error: Consumer<Throwable> = onErrorConsumer,
                                        isShowProgress: Boolean = true) {
        doAsync(successful, error, isLoadingLD, isShowProgress)
                .addSubscription()
    }

    protected fun <T> Single<T>.doAsync(successful: MutableLiveData<T>,
                                        error: Consumer<Throwable> = onErrorConsumer,
                                        isShowProgress: Boolean = true) {
        doAsync(successful, error, isLoadingLD, isShowProgress)
                .addSubscription()
    }

    protected fun <T> Observable<T>.doAsync(successful: Consumer<T>,
                                            error: Consumer<Throwable> = onErrorConsumer,
                                            isShowProgress: Boolean = true) {
        doAsync(successful, error, isLoadingLD, isShowProgress)
                .addSubscription()
    }

    protected fun <T> Observable<T>.doAsync(successful: MutableLiveData<T>,
                                            error: Consumer<Throwable> = onErrorConsumer,
                                            isShowProgress: Boolean = true) {
        doAsync(successful, error, isLoadingLD, isShowProgress)
                .addSubscription()
    }

    protected fun clearSubscription() {
        compositeDisposable?.apply {
            dispose()
            compositeDisposable = null
        }
    }

    protected fun addBackgroundSubscription(subscription: Disposable) {
        compositeDisposable?.apply {
            add(subscription)
        } ?: let {
            compositeDisposable = CompositeDisposable()
            compositeDisposable?.add(subscription)
        }
    }

    private fun parseApiException(throwable: Throwable) =
            (throwable as? ApiException)?.run {
                showMessage?.let { return it }
                errors?.takeIf { it.isNotEmpty() }?.map { it.message }?.joinToString(STRING_SPACE_VALUE)
            } ?: EMPTY_ERROR
}
