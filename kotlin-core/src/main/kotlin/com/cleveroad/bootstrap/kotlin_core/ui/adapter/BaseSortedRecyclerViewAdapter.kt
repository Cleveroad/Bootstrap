package com.cleveroad.bootstrap.kotlin_core.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import java.util.*

abstract class BaseSortedRecyclerViewAdapter<TData,
        TViewHolder : RecyclerView.ViewHolder>(context: Context) :
        RecyclerView.Adapter<TViewHolder>() {

    protected val context: Context = context.applicationContext
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected var dataComparator: Comparator<TData>? = null
    protected lateinit var listData: SortedList<TData>
    protected var sameObjectFunc: ((first: TData, second: TData) -> Boolean)? = null
    protected var sameContentFunc: ((first: TData, second: TData) -> Boolean)? = null

    constructor(context: Context,
                data: List<TData> = listOf(),
                classType: Class<TData>,
                dataComparator: Comparator<TData>? = null,
                sameObjectFunc: ((first: TData, second: TData) -> Boolean)? = null,
                sameContentFunc: ((first: TData, second: TData) -> Boolean)? = null) : this(context) {
        this@BaseSortedRecyclerViewAdapter.dataComparator = dataComparator
        this@BaseSortedRecyclerViewAdapter.sameObjectFunc = sameObjectFunc
        this@BaseSortedRecyclerViewAdapter.sameContentFunc = sameContentFunc
        this@BaseSortedRecyclerViewAdapter.listData = SortedList<TData>(classType,
                object : SortedList.Callback<TData>() {

                    override fun onInserted(position: Int, count: Int) {
                        notifyItemRangeInserted(position, count)
                        val previousZero = position + count
                        if (previousZero < itemCount) {
                            notifyItemChanged(previousZero)
                        }
                    }

                    override fun onRemoved(position: Int, count: Int) {
                        notifyItemRangeRemoved(position, count)
                    }

                    override fun onMoved(fromPosition: Int, toPosition: Int) {
                        notifyItemMoved(fromPosition, toPosition)
                    }

                    override fun onChanged(position: Int, count: Int) {
                        notifyItemRangeChanged(position, count)

                    }

                    override fun compare(o1: TData, o2: TData): Int = dataComparator?.compare(o1, o2)
                            ?: if (o1 is Comparable<*>) (o1 as Comparable<TData>).compareTo(o2) else 0

                    override fun areContentsTheSame(oldItem: TData, newItem: TData): Boolean =
                            sameContentFunc?.invoke(oldItem, newItem) ?: false

                    override fun areItemsTheSame(item1: TData, item2: TData): Boolean =
                            sameObjectFunc?.invoke(item1, item2) ?: (item1 == item2)

                })
        this@BaseSortedRecyclerViewAdapter.listData.addAll(data)
    }

    override fun getItemCount() = listData.size()

    @Throws(ArrayIndexOutOfBoundsException::class)
    fun getItem(position: Int): TData = listData[position]

    fun isEmpty() = listData.size() == 0

    fun isNotEmpty() = listData.size() != 0

    open fun add(item: TData) = listData.add(item)

    fun remove(item: TData) = listData.remove(item)

    fun remove(position: Int): TData? {
        val item = listData.get(position)
        item?.let {
            listData.remove(it)
        }
        return item
    }

    fun updateListItems(newObjects: List<TData>, callback: DiffUtil.Callback) {
        DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        listData.clear()
        listData.addAll(newObjects)
    }

    fun updateItemAt(item: TData, position: Int) = listData.updateItemAt(position, item)

    fun getItemPosition(item: TData) = listData.indexOf(item)

    open fun clear() {
        listData.clear()
    }

    open fun addAll(collection: Collection<TData>) {
        listData.apply {
            beginBatchedUpdates()
            for (item in collection) {
                add(item)
            }
            endBatchedUpdates()
        }
    }

    fun getSnapshot(): List<TData> {
        val snapShot = ArrayList<TData>()
        for (i in 0 until listData.size()) {
            snapShot.add(listData.get(i))
        }
        return snapShot
    }
}

