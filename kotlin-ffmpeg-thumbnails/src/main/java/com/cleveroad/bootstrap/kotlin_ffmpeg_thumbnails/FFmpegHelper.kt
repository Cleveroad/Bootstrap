package com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails

import android.content.Context
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

object FFmpegHelper {

    const val PERCENT_SUCCESSFULLY = 100L

    private var timePattern = Pattern.compile("time=([\\d\\w:]{8}[\\w.][\\d]+)")
    private const val SPEED = "speed"
    private const val TIME_DELIMITER = "[:|.]"

    private var isSupported = false

    private fun checkIsSupported(ffmpeg: FFmpeg): Flowable<FFmpeg> =
            Flowable.create({ emit ->
                if (isSupported) {
                    emit.onNext(ffmpeg)
                } else {
                    if (ffmpeg.isSupported) {
                        isSupported = true
                        emit.onNext(ffmpeg)
                    } else {
                        isSupported = false
                        emit.onError(UnsupportedOperationException())

                    }
                }
            }, BackpressureStrategy.BUFFER)

    fun execFFmpegBinary(command: Array<String>, context: Context, videoLengthInMillis: Long) =
            Flowable.fromCallable { FFmpeg.getInstance(context) }
                    .flatMap { checkIsSupported(it) }
                    .flatMap { ffmpeg ->
                        Flowable.create<Long>({ emit ->
                            ffmpeg.execute(command, object : ExecuteBinaryResponseHandler() {
                                override fun onFailure(s: String?) = emit.onError(Throwable(s))

                                override fun onSuccess(s: String?) {
                                    emit.onNext(PERCENT_SUCCESSFULLY)
                                    emit.onComplete()
                                }

                                override fun onProgress(s: String?) {
                                    s?.let {
                                        getProgress(s, videoLengthInMillis).takeIf { it in 1..99 }?.let {
                                            emit.onNext(it)
                                        }
                                    }
                                }

                                override fun onStart() = Unit
                                override fun onFinish() = Unit
                            })

                        }, BackpressureStrategy.LATEST)
                    }

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

    private fun timeToMs(arrayTime: List<String>): Long = (TimeUnit.HOURS.toMillis(arrayTime[0].toLong())
            + TimeUnit.MINUTES.toMillis(arrayTime[1].toLong())
            + TimeUnit.SECONDS.toMillis(arrayTime[2].toLong())
            + arrayTime[3].toLong())

}