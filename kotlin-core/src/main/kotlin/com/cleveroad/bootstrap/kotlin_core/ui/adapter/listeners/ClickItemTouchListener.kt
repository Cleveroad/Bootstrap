package com.cleveroad.bootstrap.kotlin_core.ui.adapter.listeners

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin_core.ui.NO_ITEM_ID

/**
 * Helper class for detecting click on RecyclerView's item
 */
abstract class ClickItemTouchListener internal constructor(hostView: RecyclerView) : RecyclerView.OnItemTouchListener {

    private val gestureDetector: GestureDetector = ItemClickGestureDetector(hostView.context,
            ItemClickGestureListener(hostView))

    private fun isAttachedToWindow(hostView: RecyclerView): Boolean = hostView.handler != null


    private fun hasAdapter(hostView: RecyclerView): Boolean = hostView.adapter != null

    override fun onInterceptTouchEvent(recyclerView: RecyclerView, event: MotionEvent): Boolean {
        if (!isAttachedToWindow(recyclerView) || !hasAdapter(recyclerView)) {
            return false
        }

        gestureDetector.onTouchEvent(event)
        return false
    }

    override fun onTouchEvent(recyclerView: RecyclerView, event: MotionEvent) {
        // We can silently track tap and and long presses by silently
        // intercepting touch events in the host RecyclerView.
    }

    internal abstract fun performItemClick(parent: RecyclerView, view: View, position: Int, id: Long): Boolean
    internal abstract fun performItemLongClick(parent: RecyclerView, view: View, position: Int, id: Long): Boolean

    private class ItemClickGestureDetector(context: Context, gestureListener: ItemClickGestureListener)
        : GestureDetector(context, gestureListener)

    private inner class ItemClickGestureListener(private val hostView: RecyclerView) : GestureDetector.SimpleOnGestureListener() {
        private var targetChild: View? = null

        fun dispatchSingleTapUpIfNeeded(event: MotionEvent) {
            // When the long press hook is called but the long press listener
            // returns false, the target child will be left around to be
            // handled later. In this case, we should still treat the gesture
            // as potential item click.
            targetChild?.let { onSingleTapUp(event) }
        }

        override fun onDown(event: MotionEvent): Boolean {
            val x = event.x.toInt()
            val y = event.y.toInt()

            targetChild = hostView.findChildViewUnder(x.toFloat(), y.toFloat())
            return targetChild != null
        }

        override fun onShowPress(event: MotionEvent) {
            targetChild?.isPressed = true
        }

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            var handled = false
            targetChild?.apply {
                isPressed = false
                var handledByChild = false
                if (this is ViewGroup) {
                    handledByChild = childIsClickable(this, event)
                }
                if (!handledByChild) {
                    val position = hostView.getChildAdapterPosition(this)
                    val id = hostView.adapter?.getItemId(position) ?: NO_ITEM_ID
                    handled = performItemClick(hostView, this, position, id)
                }

                targetChild = null
            }

            return handled
        }

        private fun childIsClickable(view: View, event: MotionEvent): Boolean {
            if (isPointInsideView(event.rawX, event.rawY, view)) {
                if (view is ViewGroup) {
                    for (i in 0 until view.childCount) {
                        val handledByChild = childIsClickable(view.getChildAt(i), event)
                        if (handledByChild) return true
                    }
                }
                return view.isClickable
            }
            return false
        }

        /**
         * Determines if given points are inside view
         * @param x - x coordinate of point
         * @param y - y coordinate of point
         * @param view - view object to compare
         * @return true if the points are within view bounds, false otherwise
         */
        private fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val viewX = location[0]
            val viewY = location[1]

            //point is inside view bounds
            return x > viewX && x < viewX + view.width && y > viewY && y < viewY + view.height
        }

        override fun onScroll(event: MotionEvent, event2: MotionEvent, v: Float, v2: Float): Boolean {
            return targetChild?.let {
                it.isPressed = false
                targetChild = null
                true
            } ?: false
        }

        override fun onLongPress(event: MotionEvent) {
            targetChild?.let {
                val position = hostView.getChildLayoutPosition(it)
                val id = hostView.adapter?.getItemId(position) ?: NO_ITEM_ID
                val handled = performItemLongClick(hostView, it, position, id)

                if (handled) {
                    it.isPressed = false
                    targetChild = null
                }
            } ?: return
        }
    }
}