package com.cleveroad.bootstrap.kotlin_core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.cleveroad.bootstrap.kotlin_core.utils.storage.FLAG_NOT_MODIFY_DATA_RETURN
import com.cleveroad.bootstrap.kotlin_core.utils.storage.TYPE_FILE_IMAGE
import com.cleveroad.bootstrap.kotlin_core.utils.storage.getUri
import java.io.File

class IntentUtils private constructor() {

    companion object {
        /**
         * Create image pick intent intent
         *
         * @param context instance of Context[Context]
         *
         * @return the intent[Intent]
         */
        fun createImagePickFromGallery(context: Context): Intent {
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).run {
                context.packageManager
                        .queryIntentActivities(this, FLAG_NOT_MODIFY_DATA_RETURN)
                        ?.let { if (it.isNotEmpty()) return this }
            }
            return Intent(Intent.ACTION_PICK).apply { type = TYPE_FILE_IMAGE }
        }

        /**
         * Create image pick intent from camera
         *
         * @param context    instance of Context[Context]
         * @param outFile     the out file
         * @param authority the authority of a [FileProvider] defined in a element in your app's manifest
         *
         * @return the intent[Intent]
         */
        fun createImagePickFromCamera(context: Context, outFile: () -> File, authority: String): Intent? =
                context.packageManager
                        .takeIf { it.hasSystemFeature(PackageManager.FEATURE_CAMERA) }
                        ?.let { outFile().getUri(context, authority) }
                        .let { uri ->
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                    .takeIf { it.resolveActivity(context.packageManager) != null }
                                    ?.apply { putExtra(MediaStore.EXTRA_OUTPUT, uri) }
                        }
    }
}