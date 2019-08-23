package com.cleveroad.bootstrap.kotlin_core.ui

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecorator(private val divider: Drawable,
                           private val leftMargin: Int = 0,
                           private val rightMargin: Int = 0) : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft + leftMargin
        val dividerRight = parent.width - parent.paddingRight - rightMargin

        for (i in 0 until parent.childCount) {
            with(parent.getChildAt(i)) {
                val params = layoutParams as RecyclerView.LayoutParams

                val dividerTop = bottom + params.bottomMargin
                val dividerBottom = dividerTop + divider.intrinsicHeight
                divider.apply {
                    setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                    draw(canvas)
                }
            }
        }
    }
}