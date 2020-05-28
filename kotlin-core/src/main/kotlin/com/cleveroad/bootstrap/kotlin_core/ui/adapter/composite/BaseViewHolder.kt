package com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class BaseViewHolder<TData>(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    abstract fun bind(item: TData)
}
