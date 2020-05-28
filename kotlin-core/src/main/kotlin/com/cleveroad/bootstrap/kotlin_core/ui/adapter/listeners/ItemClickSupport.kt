package com.cleveroad.bootstrap.kotlin_core.ui.adapter.listeners

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin_core.R

class ItemClickSupport private constructor(private val recyclerView: RecyclerView) {
    private val touchListener: TouchListener

    private var itemClickListener: OnItemClickListener? = null
    private var itemLongClickListener: OnItemLongClickListener? = null

    /**
     * Interface definition for a callback to be invoked when an item in the
     * RecyclerView has been clicked.
     */
    interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in the RecyclerView
         * has been clicked.
         *
         * @param parent The RecyclerView where the click happened.
         * @param view The view within the RecyclerView that was clicked
         * @param position The position of the view in the adapter.
         * @param id The row id of the item that was clicked.
         */
        fun onItemClick(parent: RecyclerView, view: View, position: Int, id: Long)
    }

    /**
     * Interface definition for a callback to be invoked when an item in the
     * RecyclerView has been clicked and held.
     */
    interface OnItemLongClickListener {
        /**
         * Callback method to be invoked when an item in the RecyclerView
         * has been clicked and held.
         *
         * @param parent The RecyclerView where the click happened
         * @param view The view within the RecyclerView that was clicked
         * @param position The position of the view in the list
         * @param id The row id of the item that was clicked
         *
         * @return true if the callback consumed the long click, false otherwise
         */
        fun onItemLongClick(parent: RecyclerView, view: View, position: Int, id: Long): Boolean
    }

    init {

        touchListener = TouchListener(recyclerView)
        recyclerView.addOnItemTouchListener(touchListener)
    }

    /**
     * Register a callback to be invoked when an item in the
     * RecyclerView has been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    /**
     * Register a callback to be invoked when an item in the
     * RecyclerView has been clicked and held.
     *
     * @param listener The callback that will be invoked.
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        recyclerView.takeIf { it.isLongClickable.not() }?.let {
            it.isLongClickable = true
        }

        itemLongClickListener = listener
    }

    private inner class TouchListener internal constructor(recyclerView: RecyclerView)
        : ClickItemTouchListener(recyclerView) {

        override fun performItemClick(parent: RecyclerView,
                                      view: View,
                                      position: Int,
                                      id: Long): Boolean = itemClickListener?.let {
            it.onItemClick(parent, view, position, id)
            true
        } ?: false


        override fun performItemLongClick(parent: RecyclerView,
                                          view: View,
                                          position: Int,
                                          id: Long): Boolean {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            return itemLongClickListener?.onItemLongClick(parent, view, position, id) ?: false
        }

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) = Unit
    }

    companion object {

        fun addTo(recyclerView: RecyclerView): ItemClickSupport {
            var itemClickSupport = from(recyclerView)
            if (itemClickSupport == null) {
                itemClickSupport = ItemClickSupport(recyclerView)
                recyclerView.setTag(R.id.twowayview_item_click_support, itemClickSupport)
            }

            return itemClickSupport
        }

        fun removeFrom(recyclerView: RecyclerView) {
            val itemClickSupport = from(recyclerView) ?: return

            recyclerView.run {
                removeOnItemTouchListener(itemClickSupport.touchListener)
                setTag(R.id.twowayview_item_click_support, null)
            }
        }

        fun from(recyclerView: RecyclerView?): ItemClickSupport? = if (recyclerView == null) {
            null
        } else {
            recyclerView.getTag(R.id.twowayview_item_click_support) as? ItemClickSupport
        }
    }
}