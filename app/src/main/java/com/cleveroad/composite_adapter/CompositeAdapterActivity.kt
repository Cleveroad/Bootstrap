package com.cleveroad.composite_adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleActivity
import com.cleveroad.bootstrap.kotlin_core.ui.NO_ID
import com.cleveroad.bootstrap.kotlin_core.ui.adapter.composite.CompositeDelegateAdapter
import com.cleveroad.composite_adapter.adapters.NotificationAdapter
import com.cleveroad.composite_adapter.adapters.NotificationEmptyAdapter
import com.cleveroad.composite_adapter.adapters.NotificationHeaderAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_composite_adapter.*
import java.util.*

class CompositeAdapterActivity : BaseLifecycleActivity<CompositeAdapterVM>() {

    companion object {
        fun start(context: Context) =
                context.startActivity(Intent(context, CompositeAdapterActivity::class.java))
    }

    override val viewModelClass = CompositeAdapterVM::class.java
    override val containerId = NO_ID
    override val layoutId = R.layout.activity_composite_adapter

    private var delegateAdapter: CompositeDelegateAdapter<Parcelable>? = null

    override fun getProgressBarId() = NO_ID

    override fun getSnackBarDuration() = Snackbar.LENGTH_SHORT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
        viewModel.loadData(Random().nextInt(20))
    }

    override fun observeLiveData() {
        viewModel.dataLD.observe(this, Observer {
            delegateAdapter?.updateAllNotify(it)
        })
    }

    private fun setupRecyclerView() {
        delegateAdapter = CompositeDelegateAdapter.Builder<Parcelable>()
                .add(NotificationAdapter())
                .add(NotificationEmptyAdapter())
                .add(NotificationHeaderAdapter())
                .build(this)
        rvNotification.apply {
            adapter = delegateAdapter
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

}
