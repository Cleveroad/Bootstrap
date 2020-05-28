package com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite

import android.content.Context
import android.os.Parcelable
import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.BaseRecyclerViewAdapter
import org.jetbrains.annotations.NotNull

class CompositeDelegateAdapter<TData: Parcelable>
private constructor(@NotNull private val typeToAdapterMap: SparseArray<DelegateAdapter<TData, RecyclerView.ViewHolder>>, context: Context)
    : BaseRecyclerViewAdapter<TData, RecyclerView.ViewHolder>(context) {

    companion object {
        private const val FIRST_VIEW_TYPE = 0
    }

    override fun getItemViewType(position: Int): Int {
        val size = typeToAdapterMap.size()
        for (i in FIRST_VIEW_TYPE until size) {
            val delegate = typeToAdapterMap.valueAt(i)
            if (delegate.isForViewType(data, position)) return typeToAdapterMap.keyAt(i)
        }
        throw IllegalArgumentException("Can not get viewType for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            typeToAdapterMap.get(viewType).onCreateViewHolder(inflater, parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        typeToAdapterMap.get(getItemViewType(position))
                ?.onBindViewHolder(holder, data, position)
                ?: throw IllegalArgumentException("Can not find adapter for position $position")
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        typeToAdapterMap.get(holder.itemViewType).onRecycled(holder)
    }

    class Builder<TData: Parcelable> {

        private var count: Int = 0
        private val typeToAdapterMap: SparseArray<DelegateAdapter<out TData, out RecyclerView.ViewHolder>> = SparseArray()

        fun add(@NotNull delegateAdapter: DelegateAdapter<out TData, out RecyclerView.ViewHolder>) =
                this.apply { typeToAdapterMap.put(count++, delegateAdapter) }

        fun build(context: Context): CompositeDelegateAdapter<TData> {
            if (count == 0) throw IllegalArgumentException("Register at least one adapter")
            return CompositeDelegateAdapter(typeToAdapterMap as SparseArray<DelegateAdapter<TData, RecyclerView.ViewHolder>>, context)
        }
    }
}