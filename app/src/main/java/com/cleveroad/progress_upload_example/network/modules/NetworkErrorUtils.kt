package com.cleveroad.progress_upload_example.network.modules

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function

object NetworkErrorUtils {
    fun <T> rxParseSingleError() = Function<Throwable, Single<T>> {
        Single.error<T>(it)
    }
}