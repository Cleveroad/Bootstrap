package com.cleveroad.composite_adapter.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite.BaseDelegateAdapter
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite.BaseViewHolder
import com.cleveroad.composite_adapter.models.NotificationEmpty


class NotificationEmptyAdapter
    : BaseDelegateAdapter<NotificationEmpty, NotificationEmptyAdapter.NotificationEmptyViewHolder>() {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
            NotificationEmptyViewHolder.newInstance(inflater, parent)

    override fun isForViewType(items: List<Parcelable>, position: Int) =
            items[position] is NotificationEmpty

    class NotificationEmptyViewHolder private constructor(view: View) : BaseViewHolder<NotificationEmpty>(view) {

        companion object {
            fun newInstance(inflater: LayoutInflater, parent: ViewGroup) =
                    NotificationEmptyViewHolder(inflater.inflate(R.layout.row_notification_empty, parent, false))
        }

        override fun bind(item: NotificationEmpty) = Unit
    }
}
