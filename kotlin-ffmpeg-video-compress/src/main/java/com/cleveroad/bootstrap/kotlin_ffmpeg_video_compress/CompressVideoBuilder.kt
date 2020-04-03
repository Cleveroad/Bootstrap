package com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress

import android.annotation.SuppressLint
import android.content.Context
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.AUDIO_BIT_RATE
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.AUDIO_RATE
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.BITRATE
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.BITS_IN_MEGABYTES
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.CODEC_COPY
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.DURATION
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.FLAG_FASTSTART
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.INPUT_ARG
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.MOVING_FLAGS
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.OVERRIDE_EXISTING
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.PERCENT_SUCCESSFULLY
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.PRESET
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.STRICT
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.TIME_DELIMITER
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.VIDEO_CODEC
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.VIDEO_CODEC_H264
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.VIDEO_CODEC_MPEG
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.VIDEO_MP4
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.Constants.VIDEO_RATE
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.FFmpegHelper.execFFmpegBinary
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.FFmpegHelper.killProcess
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.TimeUtils.timeToMs
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.types.PresetType
import com.cleveroad.bootstrap.kotlin_ffmpeg_video_compress.types.StrictType
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.regex.Pattern

/**
 * This is compression library which helps you to compress your videos and reduce file size both in default and custom way.
 *
 * It allows you to control you the approximate output file size, codecs,
 * audio and video bitrate and compress rate of input video while maintaining it quality.
 */
@SuppressLint("CheckResult")
class CompressVideoBuilder private constructor(private val context: Context) {

    companion object {
        private var bitratePattern = Pattern.compile("$BITRATE([\\d]+)")
        private var durationPattern = Pattern.compile("$DURATION([\\d\\w:]{8}[\\w.][\\d]+)")

        fun with(context: Context) = CompressVideoBuilder(context)
    }

    private var isOverride = true
    private var inputFilePath: String? = null
    private var outputFileDirPath: String? = null
    private var outputFileName: String? = null
    private var outputFileFullPath: String? = null
    private var bitrateVideo: Int? = null
    private var bitrateAudio: Int? = null
    private var videoCodec: String? = null
    private var outputFileType: String = VIDEO_MP4
    private var recommendedVideoSizeMB: Int? = null
    private var presetType: PresetType = PresetType.ULTRAFAST
    private var strictType: StrictType = StrictType.EXPERIMENTAL

    /**
     * Set input video file path
     *
     * Make sure that file exists
     * @param filename [String] - input file path
     */
    fun setInput(filename: String) = apply { inputFilePath = filename }

    /**
     * Set output file name without extension and full path in file system
     *
     * Note! If not set name or full path output file name is current time in millis
     * @param fileName [String] - input file path.
     */
    fun setOutputFileName(fileName: String) = apply { outputFileName = fileName }

    /**
     * Set output file full path name with extension.
     *
     * E.G. data/data/com.cleveroad.kotlin.bootstrap/cache
     *
     * @param path [String] - full file path
     */
    fun setOutputPath(path: String) = apply { outputFileFullPath = path }

    /**
     * Set is need to override file if exists
     *
     * @param override [Boolean] - is need to override. By default - true
     */
    fun overrideOutputFiles(override: Boolean) = apply { isOverride = override }

    /**
     * Set output directory
     * E.G. Environment.DIRECTORY_MOVIES
     *
     * Note. Don't forget to get absolute path of dir
     * @param dirPath [String] - absolute directory path. By default - cache dir
     */
    fun setOutputFileDirPath(dirPath: String) = apply { outputFileDirPath = dirPath }

    /**
     * Set output video extension
     * @param type [String] - is need to override
     */
    fun setOutputFileType(type: String) = apply { outputFileType = type }

    /**
     * Set approximate output video size in megabytes.
     *
     * By set this library calculate necessary out video bitrate.
     *
     * Make sure your file size is large than [megabytes]. Otherwise, you risk getting greater file than an input
     * @param megabytes [Int] - size in megabytes
     */
    fun setApproximateVideoSizeMb(megabytes: Int) = apply { recommendedVideoSizeMB = megabytes }

    /**
     * Set preset type to compress video file
     *
     * @param presetType [PresetType]. By default [PresetType.ULTRAFAST]
     *
     * @see [PresetType]
     */
    fun setPresetType(presetType: PresetType) = apply { this.presetType = presetType }

    /**
     * Set strict type increase/decrease compress speed
     *
     * @param strictType [StrictType]. By default [StrictType.EXPERIMENTAL]
     *
     * @see [StrictType]
     */
    fun setStrictType(strictType: StrictType) = apply { this.strictType = strictType }

    /**
     * Set custom audio and video bitrate in thousands.
     *
     * @param bitrateAudio [Int] - audio bitrate (usually 128k)
     * @param bitrateVideo [Int] - video bitrate (usually 450k)
     */
    fun setCustomBitrate(bitrateAudio: Int, bitrateVideo: Int) = apply {
        this.bitrateVideo = bitrateVideo
        this.bitrateAudio = bitrateAudio
    }

    /**
     * Set custom video codec to process video supported by ffmpeg
     *
     * @param videoCodec [String] - video codec (usually mpeg4 or h264)
     *
     * @see <a href="https://www.ffmpeg.org/general.html#Video-Codecs">Supported video codecs</a>
     */
    fun setCustomVideoCodec(videoCodec: String) = apply { this.videoCodec = videoCodec }

    /**
     * Create and execute ffmpeg command
     * @param onSuccess [String] - return output file path
     * @param onFailure [Throwable] - return throwable if file is not exist, ffmpeg not supported or ffmpeg command already running
     * @param onProgress [Long] - return progress of compressing in percentages. It can be from 0 to 100.
     */
    fun execute(onSuccess: (String) -> Unit,
                onFailure: (Throwable) -> Unit,
                onProgress: (Long) -> Unit) {
        if (inputFilePath.isNullOrEmpty() || File(inputFilePath).exists().not()) {
            onFailure.invoke(UnsupportedOperationException("Input or output files shouldn't be empty"))
            return
        }
        if (outputFileDirPath == null) outputFileDirPath = context.cacheDir.absolutePath
        if (outputFileName == null) outputFileName = System.currentTimeMillis().toString()
        val outPutFilePath = outputFileFullPath
                ?: "$outputFileDirPath${File.separator}$outputFileName.$outputFileType"
        getVideoInfo()
                .subscribe({
                    //do nothing
                }, {
                    Flowable.fromCallable { parseVideoInfo(it.localizedMessage) }
                            .flatMap { info ->
                                execFFmpegBinary(calculateComplexCommand(info.first, info.second, outPutFilePath).toTypedArray(),
                                        context,
                                        info.second)
                            }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ percents ->
                                if (percents == PERCENT_SUCCESSFULLY) {
                                    onSuccess(outPutFilePath)
                                } else {
                                    onProgress(percents)
                                }
                            }, onFailure)
                })
    }

    /**
     * Cancel ffmpeg command
     */
    fun cancel() {
        killProcess()
    }


    private fun calculateComplexCommand(bitrate: Long, duration: Long, outPath: String) = mutableListOf<String>().apply {
        if (isOverride) add(OVERRIDE_EXISTING)
        inputFilePath?.let { addAll(listOf(INPUT_ARG, it)) }
        addAll(listOf(PRESET, presetType.invoke()))
        addAll(listOf(STRICT, strictType.invoke()))
        addAll(listOf(MOVING_FLAGS, FLAG_FASTSTART))
        when {
            bitrateAudio != null && bitrateVideo != null -> {
                addAll(listOf(VIDEO_RATE, "${bitrateVideo}k", AUDIO_RATE, "${bitrateAudio}k"))
                addAll(listOf(VIDEO_CODEC, videoCodec ?: VIDEO_CODEC_H264))
            }
            recommendedVideoSizeMB != null -> {
                val recBitrate = calculateRecommendedBitrate(duration, recommendedVideoSizeMB ?: 0)
                if (recBitrate < bitrate) {
                    addAll(listOf(VIDEO_RATE, "${recBitrate}k", VIDEO_CODEC,
                            videoCodec ?: VIDEO_CODEC_H264))
                } else if (File(inputFilePath).extension == VIDEO_MP4) {
                    addAll(listOf(VIDEO_CODEC, videoCodec ?: CODEC_COPY))
                }
            }
            else -> addAll(listOf(VIDEO_CODEC, videoCodec ?: VIDEO_CODEC_MPEG))
        }
        add(outPath)
    }

    //This method always throw exception and return info in onError
    private fun getVideoInfo() = execFFmpegBinary(arrayOf(INPUT_ARG, inputFilePath).requireNoNulls(), context)

    private fun parseVideoInfo(message: String): Pair<Long, Long> =
            (bitratePattern.matcher(message) to durationPattern.matcher(message)).run {
                first.find()
                second.find()
                first.group(1).toLong() to timeToMs(second.group(1).split(TIME_DELIMITER.toRegex()).dropLastWhile { it.isEmpty() })
            }

    private fun calculateRecommendedBitrate(duration: Long, targetSize: Int) =
            ((targetSize * BITS_IN_MEGABYTES) / duration) - AUDIO_BIT_RATE

}