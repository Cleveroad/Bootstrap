package com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.model

data class VideoOptions(var filePath: String,
                        var fileLength: Long,
                        var videoLength: Long,
                        var videoFormat: String,
                        var height: Int,
                        var width: Int)