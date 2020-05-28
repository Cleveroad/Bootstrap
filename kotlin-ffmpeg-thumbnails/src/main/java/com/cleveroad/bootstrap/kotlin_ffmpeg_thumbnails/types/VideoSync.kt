package com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.types

enum class VideoSync(private val value: String) {
    //Each frame is passed with its timestamp from the demuxer to the muxer.
    PASSTHROUGH("0"),
    //Frames will be duplicated and dropped to achieve exactly the requested constant frame rate.
    CFR("cfr"),
    //Frames are passed through with their timestamp or dropped so as to prevent 2 frames from having the same timestamp.
    VFR("vfr"),
    //As passthrough but destroys all timestamps, making the muxer generate fresh timestamps based on frame-rate.
    DROP("drop"),
    //Chooses between cfr and vfr depending on muxer capabilities. This is the default method.
    AUTO("-1");

    operator fun invoke() = value
}