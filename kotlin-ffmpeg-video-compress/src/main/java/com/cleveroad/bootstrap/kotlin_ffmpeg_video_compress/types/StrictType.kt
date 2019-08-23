package com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.types

/**
 * Specify how strictly to follow the standards.
 */
enum class StrictType(private val type: String) {
    /**
     * Strictly conform to a older more strict version of the spec or reference software
     */
    VERY("very"),
    /**
     * Strictly conform to all the things in the spec no matter what consequences
     */
    STRICT("strict"),
    /**
     * Normal
     */
    NORMAL("normal"),
    /**
     * Allow unofficial extensions
     */
    UNOFFICIAL("unofficial"),
    /**
     * Allow non standardized experimental things
     */
    EXPERIMENTAL("experimental");

    operator fun invoke() = type
}