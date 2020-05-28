package com.cleveroad.compress_image

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_core.utils.IntentUtils.Companion.createImagePickFromGallery
import com.cleveroad.bootstrap.kotlin_core.utils.storage.FileUtils
import com.cleveroad.bootstrap.kotlin_core.utils.storage.ImageUtils
import com.cleveroad.bootstrap.kotlin_permissionrequest.PermissionRequest
import com.cleveroad.bootstrap.kotlin_permissionrequest.PermissionResult
import kotlinx.android.synthetic.main.activity_compress_image.*


class CompressImageActivity : AppCompatActivity() {

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 10002
        private const val REQUEST_WRITE_EXTERNAL_STORAGE = 4
        fun start(context: Context) = context.startActivity(Intent(context, CompressImageActivity::class.java))
    }

    private val permissionRequest = PermissionRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compress_image)
        bPickImage.setOnClickListener {
            permissionRequest.request(this, REQUEST_WRITE_EXTERNAL_STORAGE, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    object : PermissionResult {
                        override fun onPermissionGranted() {
                            startActivityForResult(createImagePickFromGallery(this@CompressImageActivity), PICK_IMAGE_REQUEST_CODE)
                        }
                    })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE) {
            FileUtils.copyFileAndGetRealPath(this, data?.data)?.let { filePath ->
                ImageUtils.compressImage(Uri.parse(filePath))?.let {
                    ivTestImage.setImageBitmap(it)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
