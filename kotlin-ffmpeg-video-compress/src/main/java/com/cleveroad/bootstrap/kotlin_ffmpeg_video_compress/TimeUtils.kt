package com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress

import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.PERCENT_SUCCESSFULLY
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.SPEED
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.TIME_DELIMITER
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object TimeUtils {

    private var timePattern = Pattern.compile("time=([\\d\\w:]{8}[\\w.][\\d]+)")

    fun timeToMs(arrayTime: List<String>): Long = (TimeUnit.HOURS.toMillis(java.lang.Long.parseLong(arrayTime[0]))
            + TimeUnit.MINUTES.toMillis(java.lang.Long.parseLong(arrayTime[1]))
            + TimeUnit.SECONDS.toMillis(java.lang.Long.parseLong(arrayTime[2]))
            + java.lang.Long.parseLong(arrayTime[3]))

    fun getProgress(message: String, videoLengthInMillis: Long): Long {
        if (message.contains(SPEED)) {
            return try {
                val matcher = timePattern.matcher(message)
                matcher.find()
                val tempTime = matcher.group(1)
                val currentTime = timeToMs(tempTime.split(TIME_DELIMITER.toRegex()).dropLastWhile { it.isEmpty() })
                if (videoLengthInMillis != 0L) PERCENT_SUCCESSFULLY * currentTime / videoLengthInMillis else 0
            } catch (exc: IllegalStateException) {
                0
            }
        }
        return 0
    }
}