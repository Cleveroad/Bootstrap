package com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress

import android.content.Context
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.PERCENT_SUCCESSFULLY
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.TimeUtils.getProgress
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import nl.bravobit.ffmpeg.FFtask


object FFmpegHelper {

    private var isSupported = false

    private fun checkIsSupported(ffmpeg: FFmpeg): Flowable<FFmpeg> =
            Flowable.create<FFmpeg>({ emit ->
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

    private var ffmpeg: FFmpeg? = null
    private var ffTask: FFtask? = null

    fun execFFmpegBinary(command: Array<String>, context: Context, videoLengthInMillis: Long? = null) =
            Flowable.fromCallable { FFmpeg.getInstance(context) }
                    .flatMap { checkIsSupported(it) }
                    .flatMap {
                        Flowable.create<Long>({ emit ->
                            ffmpeg = it
                            ffTask = it.execute(command, object : ExecuteBinaryResponseHandler() {
                                override fun onFailure(s: String?) = emit.onError(Throwable(s))

                                override fun onSuccess(s: String?) {
                                    emit.onNext(PERCENT_SUCCESSFULLY)
                                    emit.onComplete()
                                }

                                override fun onProgress(s: String?) {
                                    s?.takeIf { videoLengthInMillis != null }?.let {
                                        getProgress(s, videoLengthInMillis
                                                ?: 0).takeIf { it in 1..99 }?.let {
                                            emit.onNext(it)
                                        }
                                    }
                                }

                                override fun onStart() = Unit
                                override fun onFinish() = Unit
                            })

                        }, BackpressureStrategy.LATEST)
                    }

    fun killProcess() {
        ffmpeg?.apply {
            killRunningProcesses(ffTask)
        }
    }
}