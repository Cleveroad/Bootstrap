package com.cleveroad.bootstrap.kotlin_phone_input.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import androidx.core.graphics.PathParser
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

internal object BitmapUtils {

    private const val FLAG_HEIGHT_RATION = .65
    private const val FLAG_COLOR = -0x1000000

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap =
            Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888).apply {
                Canvas(this).run {
                    vectorDrawable.setBounds(0, 0, width, height)
                    vectorDrawable.draw(this)
                }
            }

    private fun getBitmap(vectorDrawable: VectorDrawableCompat): Bitmap =
            Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888).apply {
                Canvas(this).run {
                    vectorDrawable.setBounds(0, 0, width, height)
                    vectorDrawable.draw(this)
                }
            }

    fun resizeIcon(countryIcon: Drawable?, iconSize: Int, resources: Resources, flagPath: String?): BitmapDrawable? = run {
        when (countryIcon) {
            is BitmapDrawable -> countryIcon.bitmap
            is VectorDrawable -> getBitmap(countryIcon)
            is VectorDrawableCompat -> getBitmap(countryIcon)
            else -> null
        }?.let { bitmap ->
            val resBitmap = Bitmap.createScaledBitmap(bitmap, iconSize, iconSize, true)
            val flagHeight = (resBitmap.height * FLAG_HEIGHT_RATION).toInt()
            val strokeHeight = (resBitmap.height - flagHeight) / 2
            Bitmap.createBitmap(resBitmap, 0, strokeHeight, resBitmap.width, flagHeight).let { flagBitmap ->
                BitmapDrawable(resources, flagPath?.let { convertWithPath(flagBitmap, it) }
                        ?: flagBitmap)
            }
        }
    }

    private fun convertWithPath(src: Bitmap, flagPath: String): Bitmap {
        return getCroppedBitmap(src, getPath(src, flagPath))
    }

    private fun getCroppedBitmap(src: Bitmap, path: Path): Bitmap {
        val output = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = FLAG_COLOR
        canvas.drawPath(path, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(src, 0F, 0F, paint)
        return output
    }

    @SuppressLint("RestrictedApi")
    private fun getPath(src: Bitmap, flagPath: String): Path {
        return resizePath(PathParser.createPathFromPathData(flagPath),
                src.width.toFloat(),
                src.height.toFloat()
        )
    }

    private fun resizePath(path: Path, width: Float, height: Float): Path {
        val bounds = RectF(0F, 0F, width, height)
        val resizedPath = Path(path)
        val src = RectF()
        resizedPath.computeBounds(src, true)
        val resizeMatrix = Matrix()
        resizeMatrix.setRectToRect(src, bounds, Matrix.ScaleToFit.CENTER)
        resizedPath.transform(resizeMatrix)
        return resizedPath
    }
}