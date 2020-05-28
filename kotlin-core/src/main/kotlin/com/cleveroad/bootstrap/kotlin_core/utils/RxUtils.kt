package com.cleveroad.bootstrap.kotlin_core.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableTransformer
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Helper class for RxJava
 */

fun <T> ioToMain(observable: Observable<T>): Observable<T> = observable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> ioToMain(single: Single<T>): Single<T> = single
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> ioToMain(flowable: Flowable<T>): Flowable<T> = flowable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun completeToMain(completable: Completable): Completable = completable
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> ioToMainObservable() = ObservableTransformer<T, T> { ioToMain(it) }

fun <T> ioToMainSingle() = SingleTransformer<T, T> { ioToMain(it) }

fun <T> ioToMainFlowable() = FlowableTransformer<T, T> { ioToMain(it) }
