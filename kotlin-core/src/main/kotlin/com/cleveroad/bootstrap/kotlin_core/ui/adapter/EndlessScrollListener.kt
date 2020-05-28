package com.cleveroad.bootstrap.kotlin_core.ui.adapter


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin_core.utils.misc.assertInstanceOf
import com.cleveroad.bootstrap.kotlin_ext.withNotNull

class EndlessScrollListener(private val rvToScroll: RecyclerView,
                            private val visibleThreshold: Int = DEFAULT_THRESHOLD,
                            private val direction: ScrollDirection) : RecyclerView.OnScrollListener() {

    private var previousTotal: Int = 0
    @Volatile
    private var loading = true
    private var layoutManager = rvToScroll.layoutManager as? LinearLayoutManager
    private var loadMoreListener: OnLoadMoreListener? = null
    private var needToLoadMore = true
    var enabled = true

    companion object {
        private const val DEFAULT_THRESHOLD = 10
        private const val NO_POSITION = -1

        fun create(recyclerView: RecyclerView,
                   visibleThreshold: Int,
                   direction: ScrollDirection): EndlessScrollListener {
            recyclerView.layoutManager.assertInstanceOf<LinearLayoutManager>("Layout manager")
            return EndlessScrollListener(recyclerView, visibleThreshold, direction)
        }
    }

    init {
        rvToScroll.addOnScrollListener(this)
    }

    fun onLoadMoreListener(onLoadMoreListener: OnLoadMoreListener?): EndlessScrollListener {
        loadMoreListener = onLoadMoreListener
        return this
    }

    fun updateNeedToLoad(loadMore: Boolean) {
        needToLoadMore = loadMore
    }

    fun reset() {
        loading = false
        needToLoadMore = true
        previousTotal = 0
    }

    fun enable() {
        enabled = true
    }

    fun disable() {
        enabled = false
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (layoutManager == null) return
        val totalItemCount = layoutManager?.itemCount ?: 0
        val firstVisibleItem = withNotNull(layoutManager) {
            when (direction) {
                ScrollDirection.SCROLL_DIRECTION_DOWN -> findFirstCompletelyVisibleItemPosition()
                else -> findFirstVisibleItemPosition()
            }
        } ?: 0
        val visibleItemCount = rvToScroll.childCount
        if (loading
                && needToLoadMore
                && totalItemCount > previousTotal) {
            loading = false
            previousTotal = totalItemCount
        }
        if (checkForScrollDown(totalItemCount, visibleItemCount, firstVisibleItem)
                || checkForScrollUp(firstVisibleItem)) {
            loadMore()
        }
    }

    private fun checkForScrollDown(totalItemCount: Int,
                                   visibleItemCount: Int,
                                   firstVisibleItem: Int) =
            direction == ScrollDirection.SCROLL_DIRECTION_DOWN
                    && !loading
                    && firstVisibleItem != NO_POSITION
                    && needToLoadMore
                    && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)

    private fun checkForScrollUp(firstVisibleItem: Int) =
            direction == ScrollDirection.SCROLL_DIRECTION_UP
                    && !loading
                    && needToLoadMore
                    && firstVisibleItem != NO_POSITION
                    && firstVisibleItem <= visibleThreshold

    private fun loadMore() {
        loadMoreListener?.apply {
            loadMore()
            needToLoadMore = false
        }
        loading = true
    }

    enum class ScrollDirection {
        SCROLL_DIRECTION_UP, SCROLL_DIRECTION_DOWN
    }

    @FunctionalInterface
    interface OnLoadMoreListener {
        fun loadMore()
    }
}
