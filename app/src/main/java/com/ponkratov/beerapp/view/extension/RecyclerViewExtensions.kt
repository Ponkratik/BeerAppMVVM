package com.ponkratov.beerapp.view.extension

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ponkratov.beerapp.R

fun RecyclerView.addVerticalSpace(@DimenRes spaceRes: Int = R.dimen.list_vertical_space) {
    val space = context.resources.getDimensionPixelSize(spaceRes)
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position != parent.adapter?.itemCount?.dec()) {
                outRect.bottom = space
            }
        }
    })
}

fun RecyclerView.addPaginationListener(
    linearLayoutManager: LinearLayoutManager,
    itemsToLoad: Int,
    onLoadMore: () -> Unit
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
            val totalItemCount = linearLayoutManager.itemCount
            if (dy != 0 && totalItemCount <= (lastVisiblePosition + itemsToLoad)) {
                recyclerView.post(onLoadMore)
            }
        }
    })
}