package com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.types


enum class ImageType(private val type: String) {
    JPG("jpg"),
    PNG("png"),
    BMP("bmp"),
    GIF("gif");

    operator fun invoke() = type
}