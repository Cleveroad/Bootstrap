package com.cleveroad.bootstrap.kotlin_ext

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

// region Observable

fun <T> Observable<T>.workAsync(): Observable<T> =
        this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.workBackground(): Observable<T> =
        this
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())

fun <T> Observable<T>.doAsync(successful: Consumer<T>,
                              error: Consumer<Throwable>,
                              loading: MediatorLiveData<Boolean>? = null,
                              isShowProgress: Boolean = true): Disposable =
        preSubscribe(loading, isShowProgress)
                .subscribe(successful, error)

fun <T> Observable<T>.doAsync(successful: MutableLiveData<T>,
                              error: Consumer<Throwable> = Consumer { },
                              loading: MediatorLiveData<Boolean>? = null,
                              isShowProgress: Boolean = true): Disposable =
        preSubscribe(loading, isShowProgress)
                .subscribe(Consumer { successful.value = it }, error)

fun <T> Observable<T>.doAsync(successful: MutableLiveData<T>,
                              error: MutableLiveData<Throwable>,
                              loading: MediatorLiveData<Boolean>? = null,
                              isShowProgress: Boolean = true): Disposable =
        preSubscribe(loading, isShowProgress)
                .subscribe({ successful.value = it }, { error.value = it })

private fun <T> Observable<T>.preSubscribe(loading: MediatorLiveData<Boolean>?, isShowProgress: Boolean = true): Observable<T> {
    if (isShowProgress) loading?.postValue(isShowProgress)
    return workAsync().doOnEach { if (isShowProgress) loading?.hideProgress() }
}

// endregion Observable

// region Flowable

fun <T> Flowable<T>.workAsync(): Flowable<T> =
        this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.workBackground(): Flowable<T> =
        this
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())

fun <T> Flowable<T>.doAsync(successful: Consumer<T>,
                            error: Consumer<Throwable>,
                            loading: MediatorLiveData<Boolean>? = null,
                            isShowProgress: Boolean = true): Disposable =
        preSubscribe(loading, isShowProgress)
                .subscribe(successful, error)

fun <T> Flowable<T>.doAsync(successful: MutableLiveData<T>,
                            error: Consumer<Throwable>,
                            loading: MediatorLiveData<Boolean>? = null,
                            isShowProgress: Boolean = true): Disposable =
        preSubscribe(loading, isShowProgress)
                .subscribe(Consumer { successful.value = it }, error)

fun <T> Flowable<T>.doAsync(successful: MutableLiveData<T>,
                            error: MutableLiveData<Throwable>,
                            loading: MediatorLiveData<Boolean>? = null,
                            isShowProgress: Boolean = true): Disposable =
        preSubscribe(loading, isShowProgress)
                .subscribe({ successful.value = it }, { error.value = it })

private fun <T> Flowable<T>.preSubscribe(loading: MediatorLiveData<Boolean>?, isShowProgress: Boolean = true): Flowable<T> {
    if (isShowProgress) loading?.postValue(isShowProgress)
    return workAsync().doOnEach { if (isShowProgress) loading?.hideProgress() }
}


// endregion Flowable

// region Single

fun <T> Single<T>.workAsync(): Single<T> =
        this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.workBackground(): Single<T> =
        this
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())

fun <T> Single<T>.doAsync(successful: Consumer<T>,
                          error: Consumer<Throwable>,
                          loading: MediatorLiveData<Boolean>? = null,
                          isShowProgress: Boolean = true): Disposable =
        preSubscribe(loading, isShowProgress)
                .subscribe(successful, error)

fun <T> Single<T>.doAsync(successful: MutableLiveData<T>,
                          error: Consumer<Throwable>,
                          loading: MediatorLiveData<Boolean>? = null,
                          isShowProgress: Boolean = true): Disposable =
        preSubscribe(loading, isShowProgress)
                .subscribe(Consumer { successful.value = it }, error)

fun <T> Single<T>.doAsync(successful: MutableLiveData<T>,
                          error: MutableLiveData<Throwable>,
                          loading: MediatorLiveData<Boolean>? = null,
                          isShowProgress: Boolean = true): Disposable =
        preSubscribe(loading, isShowProgress)
                .subscribe({ successful.value = it }, { error.value = it })

private fun <T> Single<T>.preSubscribe(loading: MediatorLiveData<Boolean>?, isShowProgress: Boolean = true): Single<T> {
    if (isShowProgress) loading?.postValue(isShowProgress)
    return workAsync().doOnEvent { _, _ -> if (isShowProgress) loading?.hideProgress() }
}

// endregion Single


private fun MediatorLiveData<Boolean>.hideProgress() {
    postValue(false)
}