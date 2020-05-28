package com.cleveroad.bootstrap.kotlin_core.ui.adapter.multiselect

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

abstract class BaseSelectHolder<T : Selectable>(view: View, adapterCallback: MultiSelectCallback<T>) : RecyclerView.ViewHolder(view) {

    private val callbackRef = WeakReference(adapterCallback)

    /**
     * Method, that invoked, when [item] is selected
     *
     * @param item item, that was selected
     */
    abstract fun onSelected(item: T)

    /**
     * Method, that invoked, when [item] is unselected
     *
     * @param item item, that was unselected
     */
    abstract fun onUnselected(item: T)

    /**
     * Method, that invoked on [item] click.
     * Do not register your own [View.OnClickListener]'s to [itemView].
     * Instead, use this method.
     *
     * @param view [View] instance
     * @param item item, that was clicked
     */
    open fun onClick(view: View, item: T) = Unit

    /**
     * Method, that invoked on [item] long click.
     * Do not register your own [View.OnLongClickListener]'s to [itemView].
     * Instead, use this method.
     *
     * @param view [View] instance
     * @param item item, that was clicked
     */
    open fun onLongClick(view: View, item: T): Boolean = true

    @CallSuper
    open fun bind(item: T, isPickMode: Boolean) = itemView.run {
        setOnClickListener {
            onClick(it, item)
            select(item, isPickMode)
        }
        setOnLongClickListener {
            if (!isPickMode) switchPickMode(true)
            onLongClick(it, item)
        }
    }

    fun bind(item: T, isPickMode: Boolean, payload: Any?) {
        if (isPickMode) {
            (payload as? Boolean)?.let { isSelected ->
                item.isSelected = isSelected
                if (isSelected) onSelected(item) else onUnselected(item)
            }
        }
    }

    /**
     * By default, item selecting happens on item click.
     * Use this method to customize selecting behavior.
     *
     * @param item item, that was clicked
     * @param isPickMode is pick mode on
     */
    protected fun select(item: T, isPickMode: Boolean) {
        if (isPickMode) callbackRef.get()?.onSelected(item)
    }

    /**
     * By default, pick mode turning on when item long click happens.
     * Use this method to customize switching pick mode behavior.
     *
     * @param isOn is pick mode turned on
     */
    protected fun switchPickMode(isOn: Boolean) {
        callbackRef.get()?.switchPickMode(isOn)
    }
}