package com.cleveroad.bootstrap.kotlin_core.ui.adapter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleFragment
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel
import com.cleveroad.bootstrap.kotlin_core.ui.NO_ID
import com.cleveroad.bootstrap.kotlin_core.ui.VISIBLE_THRESHOLD
import com.cleveroad.bootstrap.kotlin_ext.setVisibility

/**
 * Base fragment for the implementation of the screen with a list of data.
 */
abstract class BaseListFragment<ViewModel : BaseLifecycleViewModel, M : Any> :
        BaseLifecycleFragment<ViewModel>(),
        SwipeRefreshLayout.OnRefreshListener,
        EndlessScrollListener.OnLoadMoreListener,
        PaginationListView {

    /**
     * Set id of [RecyclerView].
     */
    protected abstract val recyclerViewId: Int

    /**
     * Set id of placeholder view.
     */
    protected abstract val noResultViewId: Int

    /**
     * Set id of [SwipeRefreshLayout].
     */
    protected abstract val refreshLayoutId: Int

    /**
     * Set page limit for pagination.
     */
    protected open var pageLimit = 10

    protected open var visibleThreshold = VISIBLE_THRESHOLD

    protected var vNoResults: View? = null

    private var endlessScrollListener: EndlessScrollListener? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private lateinit var rvList: RecyclerView

    /**
     * Get an adapter that extends [BaseRecyclerViewAdapter].
     * @return an instance of [BaseRecyclerViewAdapter].
     */
    protected abstract fun getAdapter(): BaseRecyclerViewAdapter<M, *>?

    /**
     * This method is called to load the initial data.
     */
    protected abstract fun loadInitial()

    /**
     * This method is called to load the more data.
     */
    protected abstract fun loadMoreData()

    protected open fun getLayoutManager(): RecyclerView.LayoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    protected open fun getScrollDirection() =
            EndlessScrollListener.ScrollDirection.SCROLL_DIRECTION_DOWN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vNoResults = if (noResultViewId != NO_ID) view.findViewById(noResultViewId) else null
        refreshLayout = if (refreshLayoutId != NO_ID) view.findViewById(refreshLayoutId) else null
        refreshLayout?.apply {
            setOnRefreshListener(this@BaseListFragment)
        }
        initList(view)
    }

    override fun onRefresh() {
        loadInitial()
    }

    override fun loadMore() {
        loadMoreData()
    }

    /**
     * Called when need to show progress view.
     */
    protected open fun showLoadingProgress() {
        refreshLayout?.isRefreshing = true
    }

    /**
     * Called when need to hide progress view.
     */
    protected open fun hideLoadingProgress() {
        refreshLayout?.isRefreshing = false
    }

    override fun onPaginationError() {
        hideLoadingProgress()
        endlessScrollListener?.updateNeedToLoad(true)
    }

    /**
     * Set the initial data.
     */
    protected open fun onInitialDataLoaded(newData: List<M>) {
        hideLoadingProgress()
        endlessScrollListener?.reset()
        checkEndlessScroll(newData)
        getAdapter()?.apply {
            clear()
            addAll(newData)
            notifyDataSetChanged()
        }
        checkNoResults()
    }

    protected open fun invalidateNoResults() {
        checkNoResults()
    }

    /**
     * Set the range data.
     */
    protected open fun onDataRangeLoaded(newData: List<M>) {
        checkEndlessScroll(newData)
        getAdapter()?.apply {
            addAll(newData)
            if (newData.isNotEmpty()) notifyItemRangeInserted(itemCount, newData.size)
        }
        endlessScrollListener?.updateNeedToLoad(true)
    }

    /**
     * Override this method if need check empty result or not
     */
    protected open fun checkNoResults(isEmptyResult: Boolean) = Unit

    protected open fun checkNoResults() {
        (getAdapter()?.isEmpty() == true).let { isEmpty ->
            checkNoResults(isEmpty)
            vNoResults?.setVisibility(isEmpty)
        }
    }

    /**
     * Call this method if need enable pagination.
     */
    protected fun enablePagination() {
        endlessScrollListener?.enable()
    }

    /**
     * Call this method if need disable pagination.
     */
    protected fun disablePagination() {
        endlessScrollListener?.disable()
    }

    private fun initList(view: View) {
        rvList = view.findViewById(recyclerViewId)
        with(rvList) {
            adapter = this@BaseListFragment.getAdapter()
            setHasFixedSize(false)
            layoutManager = this@BaseListFragment.getLayoutManager()
            endlessScrollListener = EndlessScrollListener.create(this,
                    visibleThreshold,
                    getScrollDirection())
        }
    }

    private fun checkEndlessScroll(newData: List<M>) {
        endlessScrollListener?.onLoadMoreListener(if (newData.size < pageLimit) null else this)
    }
}