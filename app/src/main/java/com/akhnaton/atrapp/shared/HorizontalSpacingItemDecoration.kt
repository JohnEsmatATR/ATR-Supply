package com.akhnaton.atrapp.shared

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpacingItemDecoration(
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        // Space between items
        outRect.right = spacing

        // Optional: space before the first item
        if (position == 0) {
            outRect.left = 0
        }

        // Optional: remove the extra space after the last item
        if (position == itemCount - 1) {
            outRect.right = 0
        }
    }
}