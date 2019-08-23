package com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.types

/**
 * A preset is a collection of options that will provide a certain encoding speed to compression ratio
 *
 * Going from medium to slow, the time needed increases by about 40%.
 *
 * Going to slower instead would result in about 100% more time needed (i.e. it will take twice as long).
 *
 * Compared to medium, veryslow requires 280% of the original encoding time, with only minimal improvements over slower in terms of quality.
 *
 * Using fast saves about 10% encoding time, faster 25%. ultrafast will save 55% at the expense of much lower quality.
 */
enum class PresetType(private val type: String) {
    ULTRAFAST("ultrafast"),
    SUPERFAST("superfast"),
    VERYFAST("veryfast"),
    FASTER("faster"),
    FAST("fast"),
    MEDIUM("medium"),
    SLOW("slow"),
    SLOWER("slower"),
    VERYSLOW("veryslow");

    operator fun invoke() = type
}