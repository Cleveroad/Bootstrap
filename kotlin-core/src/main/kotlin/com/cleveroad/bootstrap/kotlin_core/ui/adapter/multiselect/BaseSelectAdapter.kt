package com.cleveroad.bootstrap.kotlin_core.ui.adapter.multiselect

import android.content.Context
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.BaseRecyclerViewAdapter
import com.cleveroad.bootstrap.kotlin_ext.forEachExcluding

abstract class BaseSelectAdapter<T : Selectable,
        VH : BaseSelectHolder<T>>(context: Context,
                                  isPickMode: Boolean = false,
                                  isMultiSelectMode: Boolean = true,
                                  data: List<T> = listOf()) : BaseRecyclerViewAdapter<T, VH>(context, data), MultiSelectCallback<T> {

    companion object {

        const val NO_POS = -1
    }

    /**
     * @return is pick mode on
     */
    var isPickMode = isPickMode
        private set

    /**
     * @return is multi select mode on
     */
    var isMultiSelectMode = isMultiSelectMode
        private set

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), isPickMode)
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        payloads.takeIf { it.isNotEmpty() }?.let {
            holder.bind(data[position], isPickMode, it.firstOrNull())
        } ?: super.onBindViewHolder(holder, position, payloads)
    }

    /**
     * Switch recycler view pick mode.
     *
     * @param isOn is pick mode on
     */
    override fun switchPickMode(isOn: Boolean) {
        isPickMode = isOn
        notifyDataSetChanged()
    }

    override fun onSelected(item: T) {
        if (isMultiSelectMode) multiSelect(item) else singleSelect(item)
    }

    /**
     * Turn on multi select mode.
     */
    fun turnOnMultiSelectMode() {
        isMultiSelectMode = true
    }

    /**
     * Turn off multi select mode.
     *
     * @param selectedPosition position of element, that remains selected.
     * By default, it is first selected item.
     */
    fun turnOffMultiSelectMode(selectedPosition: Int = firstSelected()) {
        data.forEachIndexed { index, item ->
            item.takeIf { it.isSelected && index != selectedPosition }?.run {
                isSelected = false
                notifyItemChanged(index)
            }
        }
    }

    private fun multiSelect(item: T) {
        item.isSelected = !item.isSelected
        data.indexOf(item).takeUnless { it == NO_POS }?.let { position ->
            notifyItemChanged(position, item.isSelected)
        }
    }

    private fun singleSelect(item: T) {
        item.isSelected = !item.isSelected
        data.forEachExcluding(item) {
            it.isSelected = false
        }
        notifyDataSetChanged()
    }

    private fun firstSelected() = data.indexOfFirst { it.isSelected }
}