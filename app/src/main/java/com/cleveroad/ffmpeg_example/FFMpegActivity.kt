package com.cleveroad.ffmpeg_example

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.utils.storage.FileUtils
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.ThumbnailsFFMpegBuilder
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.model.Result
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.CompressVideoBuilder
import com.cleveroad.bootstrap.kotlin_permissionrequest.PermissionRequest
import com.cleveroad.bootstrap.kotlin_permissionrequest.PermissionResult
import kotlinx.android.synthetic.main.activity_ffmpeg_thumbnails.*
import kotlinx.android.synthetic.main.include_progress.*
import java.io.File

class FFMpegActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "FFMpegActivity"
        private const val PICK_VIDEO = "video/*"
        private const val PICK_IMAGE = "image/*"

        private const val THUMBNAILS_COUNT = 100
        private const val REQUEST_PICK_VIDEO = 10004
        private const val REQUEST_PICK_OVERLAY = 10006
        private const val REQUEST_WRITE_EXTERNAL_STORAGE = 4
        private const val DEFAULT_OUTPUT_VIDEO_EXTENSION = ".mp4"

        fun start(context: Context) = context.startActivity(Intent(context, FFMpegActivity::class.java))
    }

    private val permissionRequest = PermissionRequest()
    private val photosAdapter by lazy { PhotosAdapter(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg_thumbnails)
        initPhotosRV()
        setClickListeners(bPickFile, bPickOverlayFile, bCreateThumbnails, bCompressVideo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                when (requestCode) {
                    REQUEST_PICK_VIDEO -> tvPickedFile.text = FileUtils.getRealPath(this, it)
                    REQUEST_PICK_OVERLAY -> tvPickedOverlayFile.text = FileUtils.getRealPath(this, it)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bPickFile -> {
                requestWriteStoragePermission {
                    startActivityForResult(intentForPickVideo(), REQUEST_PICK_VIDEO)
                }
            }
            R.id.bPickOverlayFile -> {
                requestWriteStoragePermission {
                    startActivityForResult(intentForPickImage(), REQUEST_PICK_OVERLAY)
                }
            }
            R.id.bCreateThumbnails -> {
                requestWriteStoragePermission {
                    setProgressVisibility(true)
                    ThumbnailsFFMpegBuilder.with(this)
                            .setInput(tvPickedFile.text.toString())
                            .setOutput(etFileName.text.toString())
                            .setThumbnailsCount(THUMBNAILS_COUNT)
                            .execute({ result -> onResultThumbnails(result) },
                                    { error -> onError(error) },
                                    { progress -> onProgress(progress) })
                }
            }
            R.id.bCompressVideo -> {
                requestWriteStoragePermission {
                    setProgressVisibility(true)
                    CompressVideoBuilder.with(this)
                            .setInput(tvPickedFile.text.toString())
                            .setOutputPath(createTempVideoFile("temp").absolutePath)
                            .setApproximateVideoSizeMb(10)
                            .execute({ result -> onResultCompressVideo(result) },
                                    { error -> onError(error) },
                                    { progress -> onProgress(progress) })
                }
            }
        }
    }

    private fun onResultThumbnails(result: Result) {
        photosAdapter.apply {
            clear()
            addAll(result.thumbnails)
            notifyDataSetChanged()
        }
        setProgressVisibility(false)
    }

    private fun onResultCompressVideo(pathname: String) {
        val inputSize = File(tvPickedFile.text.toString()).length()
        val outputSize = File(pathname).length()
        setProgressVisibility(false)
        tvPickedOverlayFile.text = "Picked file size - $inputSize\n" +
                "Output file size - $outputSize\n" +
                "Reduce difference - ${inputSize / outputSize}"
    }

    private fun onError(error: Throwable) {
        setProgressVisibility(false)
        error.message?.let { Log.e(TAG, it) }
    }

    private fun onProgress(progress: Long) {
        tvProgress.text = progress.toString()
    }

    private fun intentForPickVideo() = Intent().apply {
        type = PICK_VIDEO
        action = Intent.ACTION_PICK
    }

    private fun intentForPickImage() = Intent().apply {
        type = PICK_IMAGE
        action = Intent.ACTION_PICK
    }

    private fun requestWriteStoragePermission(onGranted: () -> Unit) {
        permissionRequest.request(this, REQUEST_WRITE_EXTERNAL_STORAGE, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                object : PermissionResult {
                    override fun onPermissionGranted() {
                        onGranted.invoke()
                    }
                })
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        progressView.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun initPhotosRV() {
        with(rvPhotos) {
            invalidate()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = photosAdapter
        }
    }

    //For testing
    private fun createTempVideoFile(name: String, extension: String = DEFAULT_OUTPUT_VIDEO_EXTENSION): File {
        val filePath = "$name$extension"
        var dest = File(cacheDir, filePath)
        var fileNo = 0
        while (dest.exists()) {
            fileNo++
            dest = File(cacheDir, fileNo.toString() + filePath)
        }
        return dest
    }
}
