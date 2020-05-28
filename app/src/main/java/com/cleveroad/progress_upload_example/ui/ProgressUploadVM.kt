package com.cleveroad.progress_upload_example.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel
import com.cleveroad.progress_upload_example.network.modules.NetworkModule
import com.cleveroad.progress_upload_example.utils.NotificationUtils
import io.reactivex.rxjava3.functions.Consumer
import okhttp3.MultipartBody

class ProgressUploadVM(application: Application) : BaseLifecycleViewModel(application) {

    val imageLinkLD = MutableLiveData<String>()

    private val imageModule = NetworkModule.image

    fun uploadImage(image: MultipartBody.Part) {
        imageModule.uploadImage(image)
            .map { it?.link }
            .doAsync(Consumer {
                NotificationUtils().updateSuccessStatus()
            }, Consumer {
                NotificationUtils().updateErrorStatus()
            })
    }
}