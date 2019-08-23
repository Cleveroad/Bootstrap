package com.cleveroad.bootstrap.kotlin_phone_input.data.models

import android.os.Parcel
import android.os.Parcelable

internal interface KParcelable : Parcelable {
    override fun describeContents(): Int = 0

    companion object {
        inline fun <reified T : Any> generateCreator(crossinline create: (source: Parcel) -> T): Parcelable.Creator<T> = object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel): T = create(source)

            override fun newArray(size: Int): Array<out T?> = arrayOfNulls<T>(size)
        }
    }
}

internal inline fun <reified T> Parcel.read(): T = readValue(T::class.javaClass.classLoader) as T
internal fun Parcel.write(vararg values: Any?) = values.forEach { writeValue(it) }