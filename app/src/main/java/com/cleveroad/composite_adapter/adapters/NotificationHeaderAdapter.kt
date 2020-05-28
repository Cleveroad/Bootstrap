package com.cleveroad.composite_adapter.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite.BaseDelegateAdapter
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite.BaseViewHolder
import com.cleveroad.composite_adapter.models.NotificationHeader
import kotlinx.android.synthetic.main.row_notification_header.view.*


class NotificationHeaderAdapter
    : BaseDelegateAdapter<NotificationHeader, NotificationHeaderAdapter.NotificationHeaderViewHolder>() {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
            NotificationHeaderViewHolder.newInstance(inflater, parent)

    override fun isForViewType(items: List<Parcelable>, position: Int) =
            items[position] is NotificationHeader

    class NotificationHeaderViewHolder private constructor(view: View) : BaseViewHolder<NotificationHeader>(view) {

        companion object {
            fun newInstance(inflater: LayoutInflater, parent: ViewGroup) =
                    NotificationHeaderViewHolder(inflater.inflate(R.layout.row_notification_header, parent, false))
        }

        override fun bind(item: NotificationHeader) {
            itemView.tvNotificationHeader.setText(item.resId)
        }
    }
}
