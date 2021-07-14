package com.dupat.newsqu.ui.adapter.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {



    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = spaceHeight
        outRect.right = spaceHeight
        outRect.left = spaceHeight
        if (parent.getChildAdapterPosition(view) == parent.adapter?.itemCount!! - 1) {
            outRect.bottom = spaceHeight;
        }
    }

}