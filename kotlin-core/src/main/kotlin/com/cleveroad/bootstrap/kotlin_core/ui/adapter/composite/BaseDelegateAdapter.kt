package com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull

abstract class BaseDelegateAdapter<TData : Parcelable, VH : BaseViewHolder<TData>> : DelegateAdapter<TData, VH> {

    @NonNull
    protected abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

    override fun onRecycled(holder: VH) = Unit

    @NonNull
    override fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup) =
            createViewHolder(inflater, parent)

    override fun onBindViewHolder(@NonNull holder: VH, @NonNull items: List<TData>, position: Int) =
            holder.bind(items[position])

}
