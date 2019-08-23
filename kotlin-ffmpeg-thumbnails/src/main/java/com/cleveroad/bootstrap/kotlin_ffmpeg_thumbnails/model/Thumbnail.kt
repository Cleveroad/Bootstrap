package com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.model

import android.graphics.Bitmap

data class Thumbnail(var fileLength: Long,
                     var filePath: String,
                     var imageFormat: String,
                     var timeFromVideo: Long,
                     var bitmap: Bitmap)