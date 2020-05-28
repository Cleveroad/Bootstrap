package com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress

object Constants {

    //Common
    const val TIME_DELIMITER = "[:|.]"
    const val AUDIO_BIT_RATE = 128
    const val BITRATE = "bitrate: "
    const val DURATION = "Duration: "
    const val VIDEO_CODEC_MPEG = "mpeg4"
    const val AUDIO_CODEC_AAC = "aac"
    const val VIDEO_MP4 = "mp4"
    const val VIDEO_CODEC_H264 = "h264"
    const val SPEED = "speed"
    const val FLAG_FASTSTART = "faststart"
    const val PERCENT_SUCCESSFULLY = 100L
    const val BITS_IN_MEGABYTES = 1000000 * 8

    //Commands
    const val VIDEO_CODEC = "-vcodec"
    const val AUDIO_CODEC = "-acodec"
    const val VIDEO_RATE = "-b:v"
    const val AUDIO_RATE = "-b:a"
    const val CODEC_COPY = "copy"
    const val PRESET = "-preset"
    const val STRICT = "-strict"
    const val INPUT_ARG = "-i"
    const val MOVING_FLAGS = "-movflags"
    const val OVERRIDE_EXISTING = "-y"
}