package com.cleveroad.bootstrap.kotlin_core.utils.storage


import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.cleveroad.bootstrap.kotlin_core.utils.storage.TypeDirPath.CACHE
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

/**
 * Provides common methods of the utility for working with image
 */
class ImageUtils private constructor() {

    companion object {

        /**
         * The image is compressed if its size is greater than [maxSize]
         * or its resolution is greater than [maxResolution]. Maximum resolution of the image by default [MAX_IMAGE_RESOLUTION] and
         * maximum size of the image by default [MAX_IMAGE_SIZE].
         *
         * @param imageUri image's uri[Uri]
         * @param maxResolution maximum resolution image
         * @param maxSize maximum size image
         *
         * @return image file[File]
         */
        fun compressImage(imageUri: Uri,
                          maxResolution: Int = MAX_IMAGE_RESOLUTION,
                          maxSize: Double = MAX_IMAGE_SIZE): Bitmap? =
                File(imageUri.path).takeIf { it.exists() }?.let { file ->
                    with(getImageSize(imageUri)) {
                        when {
                            file.length() > maxSize -> {
                                (file.length() / maxSize).let { reductionCoef ->
                                    decodeBitmap(file, outWidth.toDouble() / reductionCoef,
                                            outHeight.toDouble() / reductionCoef).run {
                                        max(width, height).takeIf { it > maxResolution }?.let {
                                            with(max(width, height).toDouble() / maxResolution) {
                                                resizeBitmap(this@run, width / this, height / this)
                                            }
                                        } ?: this@run
                                    }
                                }
                            }
                            max(outWidth, outHeight) > maxResolution -> {
                                with(max(outWidth, outHeight).toDouble() / maxResolution) {
                                    decodeBitmap(file, outWidth / this, outHeight / this)
                                }
                            }
                            else -> BitmapFactory.decodeFile(file.path)
                        }
                    }
                }

        /**
         * Save bitmap[Bitmap] to file[File]
         *
         * @param destinationFile destination file[File]
         * @param bitmap bitmap[Bitmap]
         * @param quality compress quality by default [COMPRESS_QUALITY]
         * @param recycle recycle Bitmap in the end. Default = true
         *
         * @return returns true if the file is saved successfully otherwise false
         */
        fun saveBitmap(destinationFile: File?,
                       bitmap: Bitmap?,
                       quality: Int = COMPRESS_QUALITY,
                       recycle: Boolean = true): Boolean {
            try {
                FileOutputStream(destinationFile).use { bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, it) }
            } catch (e: Exception) {
                Log.e("Error", e.message)
                return false
            } finally {
                if (recycle) bitmap?.recycle()
            }
            return true
        }

        /**
         * Create temporary image file with extension .jpg
         *
         * @param path directory path
         * @param datePattern date pattern by default[DATE_PATTERN]
         *
         * @return image file[File]. May return null if path is null
         */
        fun createImageFile(path: String?,
                            fileName: String?,
                            datePattern: String = DATE_PATTERN): File? =
                path?.run {
                    val timeStamp = fileName
                            ?: SimpleDateFormat(datePattern, Locale.getDefault()).format(Date())
                    val dir = FileUtils.createDir(path, clearIfExist = false)

                    File.createTempFile(timeStamp, IMAGE_FORMAT_JPG, dir)
                }


        /**
         * Create temporary image file with extension .jpg
         *
         * @param context instance of Context[Context]
         * @param dirType type of directory [TypeDirPath]
         * @param datePattern date pattern by default[DATE_PATTERN]
         *
         * @return image file[File]. May return null if path is null
         */
        fun createImageFile(context: Context,
                            dirType: TypeDirPath = CACHE,
                            fileName: String?,
                            datePattern: String = DATE_PATTERN): File? =
                createImageFile(dirType.path(context), fileName, datePattern)

        /**
         * Get bitmap option[BitmapFactory.Options]
         *
         * @param path image file path
         *
         * @return bitmap option[BitmapFactory.Options]
         */
        fun getImageSize(path: String) = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, this)
        }

        /**
         * Get bitmap option[BitmapFactory.Options]
         *
         * @param file image file[File]
         *
         * @return bitmap option[BitmapFactory.Options]
         */
        fun getImageSize(file: File) = getImageSize(file.absolutePath)

        /**
         * Get bitmap option[BitmapFactory.Options] by image uri[Uri]
         *
         * @param uri image uri[Uri]
         *
         * @return bitmap option[BitmapFactory.Options]
         */
        fun getImageSize(uri: Uri) = getImageSize(File(uri.path))

        /**
         * Decode image from file path for resolution
         *
         * @param path image file path
         * @param targetWidth width image
         * @param targetHeight height image
         *
         * @return bitmap option[Bitmap]
         */
        private fun decodeBitmap(path: String,
                                 targetWidth: Double,
                                 targetHeight: Double): Bitmap =
                with(BitmapFactory.Options()) {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeFile(path, this)

                    // Calculate inSampleSize
                    inSampleSize = calculateInSampleSize(this, targetWidth, targetHeight)

                    // Decode bitmap with inSampleSize set
                    inJustDecodeBounds = false
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                    BitmapFactory.decodeFile(path, this)
                }

        /**
         * Decode image from file [File] for resolution
         *
         * @param file image file [File]
         * @param targetWidth width image
         * @param targetHeight height image
         *
         * @return bitmap option[Bitmap]
         */
        private fun decodeBitmap(file: File,
                                 targetWidth: Double,
                                 targetHeight: Double): Bitmap =
                decodeBitmap(file.absolutePath, targetWidth, targetHeight)

        /**
         * Find scale[BitmapFactory.Options.inSampleSize] for the resolution
         *
         * @param options bitmap option[BitmapFactory.Options]
         * @param targetWidth width image
         * @param targetHeight height image
         *
         * @return scale[BitmapFactory.Options.inSampleSize]
         */
        private fun calculateInSampleSize(options: BitmapFactory.Options,
                                          targetWidth: Double,
                                          targetHeight: Double): Int {
            // Raw height and width of image
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = IN_SAMPLE_SIZE
            if (height > targetHeight || width > targetWidth) {
                do {
                    inSampleSize *= RESOLUTION_REDUCTION_COEFFICIENT
                } while (inSampleSize != 0 && (height / inSampleSize >= targetHeight && width / inSampleSize >= targetWidth))
            }

            return inSampleSize
        }

        /**
         * Get image's uri[Uri] from file
         *
         * @param context instance of Context[Context]
         * @param imageFile image's file[File]
         *
         * @return image's uri[Uri]
         */
        fun getImageUri(context: Context,
                        imageFile: File): Uri? =
                context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID),
                        "${MediaStore.Images.Media.DATA}=? ", arrayOf(imageFile.absolutePath), null).use {
                    when {
                        it?.moveToFirst() == true -> {
                            val id = it.getInt(it.getColumnIndex(MediaStore.MediaColumns._ID))
                            Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id))
                        }
                        imageFile.exists() -> {
                            val values = ContentValues()
                            values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                        }
                        else -> null
                    }
                }

        /**
         * Modify bitmap size.
         * @return Returned new resized bitmap
         */
        fun resizeBitmap(bitmap: Bitmap,
                         newWidth: Double,
                         newHeight: Double): Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            // Create a matrix for manipulation
            val matrix = Matrix()
            // Resize the bitmap
            matrix.postScale(scaleWidth, scaleHeight)
            // Create bitmap with new parameters
            val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
            bitmap.recycle()
            return resizedBitmap
        }

        /**
         * Modify orientation image to normal orientation [ExifInterface.ORIENTATION_NORMAL]
         * <br/>Used [rotate] method
         * @param bitmap bitmap[Bitmap]
         * @param path absolute file path
         *
         * @return new bitmap [Bitmap] with normal orientation [ExifInterface.ORIENTATION_NORMAL]
         */
        fun rotateToNormal(bitmap: Bitmap,
                           path: String): Bitmap {
            val ei = ExifInterface(path)
            val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)
            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90F)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180F)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270F)
                else -> bitmap
            }
        }

        /**
         * Rotates bitmap to a specific degree
         *
         * @param bitmap bitmap[Bitmap]
         * @param degree degree
         * @param mirror use mirror effect flag
         * @param recycle recycle old bitmap flag
         *
         * @return new rotated bitmap [Bitmap]
         */
        fun rotate(bitmap: Bitmap,
                   degree: Float,
                   mirror: Boolean = false,
                   recycle: Boolean = false): Bitmap = with(Matrix()) {
            if (mirror) preScale(1F, -1F)
            postRotate(degree)
            Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    this,
                    true).apply { if (recycle) bitmap.recycle() }
        }
    }
}
