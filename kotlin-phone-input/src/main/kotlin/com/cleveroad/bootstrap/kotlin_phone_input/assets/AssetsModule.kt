package com.cleveroad.bootstrap.kotlin_phone_input.assets

import android.content.Context
import io.reactivex.rxjava3.core.Single
import org.json.JSONArray
import java.io.IOException

internal interface AssetsModule {
    fun getCountries(context: Context): Single<JSONArray>
}

internal object AssetsModuleImpl : AssetsModule {

    private const val COUNTRY_CODES = "country_codes.json"

    override fun getCountries(context: Context): Single<JSONArray> = Single.just(Unit)
            .map {
                try {
                    context.assets.open(COUNTRY_CODES).use {
                        JSONArray(String(it.readBytes(), Charsets.UTF_8))
                    }
                } catch (ex: IOException) {
                    JSONArray()
                }
            }
}
