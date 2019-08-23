package com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.types

enum class ExtractFrequency {
    EVERY_SECOND,
    EVERY_TEN_SECOND,
    EVERY_THIRTY_SECOND,
    EVERY_MINUTE,
    ALL_FRAMES,
    CUSTOM,
    FIXED_FRAME_COUNT,
    SINGLE;
}