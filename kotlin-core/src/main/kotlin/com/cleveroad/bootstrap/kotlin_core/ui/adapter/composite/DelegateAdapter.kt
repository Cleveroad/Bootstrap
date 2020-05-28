package com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

interface DelegateAdapter<TData : Parcelable, VH : RecyclerView.ViewHolder> {

    @NonNull
    fun onCreateViewHolder(@NonNull inflater: LayoutInflater, @NonNull parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(@NonNull holder: VH, @NonNull items: List<TData>, position: Int)

    fun onRecycled(holder: VH)

    fun isForViewType(@NonNull items: List<Parcelable>, position: Int): Boolean
}