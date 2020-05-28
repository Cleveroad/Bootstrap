package com.cleveroad.bootstrap.kotlin_core.ui.adapter

import androidx.recyclerview.widget.DiffUtil

abstract class BaseDiffUtilCallback<TData>(private var oldList: List<TData> = listOf(),
                                           private var newList: List<TData> = listOf()) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition] ?: false
}
