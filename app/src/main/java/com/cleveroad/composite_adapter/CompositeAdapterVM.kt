package com.cleveroad.composite_adapter

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel
import com.cleveroad.composite_adapter.models.Notification
import com.cleveroad.composite_adapter.models.NotificationEmpty
import com.cleveroad.composite_adapter.models.NotificationHeader
import io.reactivex.rxjava3.core.Single
import java.util.*


class CompositeAdapterVM(application: Application) : BaseLifecycleViewModel(application) {

    val dataLD = MutableLiveData<List<Parcelable>>()

    fun loadData(count: Int) {
        Single.just(count)
                .map { mockDataFactory(it) }
                .doAsync(dataLD)
    }

    private fun mockDataFactory(count: Int) = mutableListOf<Parcelable>()
            .apply {
                val random = Random()
                for (i in 0 until count) {
                    add(when (random.nextInt(3)) {
                        0 -> Notification(R.string.notification_title, R.string.some_kind_of_message)
                        1 -> NotificationHeader(R.string.notification_header)
                        else -> NotificationEmpty()
                    })
                }
            }
            .toList()
}
