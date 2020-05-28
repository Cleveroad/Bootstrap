package com.cleveroad.progress_upload_example.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleActivity
import com.cleveroad.bootstrap.kotlin_core.ui.NO_ID
import com.cleveroad.bootstrap.kotlin_core.utils.storage.FileUtils
import com.cleveroad.bootstrap.kotlin_core.utils.storage.TypeDirPath
import com.cleveroad.bootstrap.kotlin_progress_upload.counting.CountingProgressCallback
import com.cleveroad.bootstrap.kotlin_progress_upload.extensions.getMultipartCounting
import com.cleveroad.progress_upload_example.utils.NotificationUtils
import kotlinx.android.synthetic.main.activity_progress_upload.*
import java.io.File

class ProgressUploadActivity : BaseLifecycleActivity<ProgressUploadVM>(), CountingProgressCallback {

    companion object {
        fun start(context: Context) = context.run {
            startActivity(Intent(this, ProgressUploadActivity::class.java))
        }
    }

    override val containerId = NO_ID

    override val layoutId = R.layout.activity_progress_upload

    override val viewModelClass = ProgressUploadVM::class.java

    private val notificationUtils = NotificationUtils()

    override fun getProgressBarId() = NO_ID

    override fun getSnackBarDuration() = NO_ID

    override fun observeLiveData() = viewModel.run {
        imageLinkLD.observe(this@ProgressUploadActivity, Observer {
            notificationUtils.updateSuccessStatus()
        })

        errorLD.observe(this@ProgressUploadActivity, Observer {
            notificationUtils.updateErrorStatus()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bOpenGallery.setOnClickListener { openGallery() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode.GALLERY_REQUEST_CODE()) {
            data?.data?.let {
                FileUtils.copyFile(application, it, TypeDirPath.CACHE)?.let { path ->
                    viewModel.uploadImage(File(path.absolutePath).getMultipartCounting(
                        FORM_DATA_IMAGE, countingCallback = this@ProgressUploadActivity))
                }
            }
        }
    }

    override fun onProgressChanged(progress: Float) {
        notificationUtils.showNotification(progress.toInt())
    }

    private fun openGallery() {
        Intent().apply {
            type = MIME_TYPE_IMAGE
            action = Intent.ACTION_GET_CONTENT
            packageManager?.let { packageManager ->
                Intent.createChooser(this, getString(R.string.pick_image))
                    .takeIf { it.resolveActivity(packageManager) != null }
                    ?.let { startActivityForResult(it, RequestCode.GALLERY_REQUEST_CODE()) }
            }
        }
    }
}