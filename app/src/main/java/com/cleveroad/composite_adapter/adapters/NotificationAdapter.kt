package com.cleveroad.composite_adapter.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite.BaseDelegateAdapter
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite.BaseViewHolder
import com.cleveroad.composite_adapter.models.Notification
import kotlinx.android.synthetic.main.row_notification.view.*


class NotificationAdapter
    : BaseDelegateAdapter<Notification, NotificationAdapter.NotificationItemViewHolder>() {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
            NotificationItemViewHolder.newInstance(inflater, parent)

    override fun isForViewType(items: List<Parcelable>, position: Int) =
            items[position] is Notification

    class NotificationItemViewHolder private constructor(view: View) : BaseViewHolder<Notification>(view) {

        companion object {
            fun newInstance(inflater: LayoutInflater, parent: ViewGroup) =
                    NotificationItemViewHolder(inflater.inflate(R.layout.row_notification, parent, false))
        }

        override fun bind(item: Notification) {
            itemView.apply {
                tvNotificationTitle.setText(item.titleId)
                tvNotificationMessage.setText(item.messageId)
            }
        }
    }
}
