package com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.FFmpegHelper.PERCENT_SUCCESSFULLY
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.cache.ResultCache
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.model.Result
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.model.Thumbnail
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.model.VideoOptions
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.types.ExtractFrequency
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.types.ExtractFrequency.*
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.types.ImageType
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.types.VideoSync
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class ThumbnailsFFMpegBuilder private constructor(private val context: Context) {

    companion object {
        private const val TAG = "ThumbnailsFFMpegBuilder"
        //Commands
        private const val INPUT_ARG = "-i"
        private const val VIDEO_FILTER = "-filter_complex"
        private const val START_SECONDS = "-ss"
        private const val OVERRIDE_EXISTING = "-y"
        private const val VIDEO_FRAME = "-vframes"
        private const val VIDEO_SYNC_METHOD = "-vsync"
        private const val OVERLAY_PICT = "overlay="
        private const val QUALITY_SCALE = "-qscale"
        private const val FPS_SET = "fps="
        private const val SCALE_SET = "scale="
        private const val SELECT_ALL_FRAMES = "select=eq(pict_type\\,I)"
        //Defaults
        private const val SINGLE_FRAME = "1"
        private const val DEFAULT_THUMBNAIL_COUNT = 10
        private const val DEFAULT_FRAME_COUNT = 60.0
        private const val DEFAULT_FILE_TYPE = "jpg"
        private const val DEFAULT_FILES_PATTERN = "%04d"
        private const val DEFAULT_SCALE_SIZE = "100x100"
        //In center of main picture
        private const val DEFAULT_OVERLAY_POSITION = "x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2"
        private const val DEFAULT_QUALITY_SCALE = 2
        private const val DEFAULT_QUALITY__MIN_SCALE = 1
        private const val DEFAULT_QUALITY__MAX_SCALE = 31
        //Take frame every minute
        private const val DEFAULT_CUSTOM_FPS = "1/$DEFAULT_FRAME_COUNT"

        fun with(context: Context) = ThumbnailsFFMpegBuilder(context)
    }

    private var isOverride = true
    private var inputFilePath: String? = null
    private var outputFileDirPath: String? = null
    private var outputFileName: String? = null
    private var outputFileType: String = DEFAULT_FILE_TYPE
    private var outputThumbnailCount: Int = DEFAULT_THUMBNAIL_COUNT
    private var complexCommand = mutableListOf<String>()
    private var frequency: ExtractFrequency = ALL_FRAMES
    private var singleFrameTakeTime: String? = null
    private var customFPS: String = DEFAULT_CUSTOM_FPS
    private var videoSyncMethod: VideoSync = VideoSync.VFR
    private var scaleSize: String = DEFAULT_SCALE_SIZE
    private var videoFilters = mutableListOf<String>()
    private var overlayPicPath: String? = null
    private var overlayPicCoordinates: String? = null
    private var qualityScale: Int = DEFAULT_QUALITY_SCALE

    fun setInput(filename: String) = apply { inputFilePath = filename }

    fun setOutput(filePath: String) = apply { outputFileName = filePath }

    /**
     * Set rescale images size width and height in pixels
     */
    fun setScaleSize(width: Int, height: Int) = apply { scaleSize = "${width}x$height" }

    /**
     * Set overlay position regarding  thumbnails size
     */
    fun setOverlayCoordinates(x: Int, y: Int) = apply { overlayPicCoordinates = "x=$x:y=$y" }

    fun overrideOutputFiles(override: Boolean) = apply { isOverride = override }

    fun setThumbnailsCount(count: Int) = apply {
        frequency = FIXED_FRAME_COUNT
        outputThumbnailCount = count
    }

    /**
     * Set frequency of extract frames. By default [frequency] - [ExtractFrequency.ALL_FRAMES]
     */
    fun setFrequency(freq: ExtractFrequency) = apply { frequency = freq }

    fun setVideoSyncMethod(sync: VideoSync) = apply { videoSyncMethod = sync }

    /**
     * [quality] - This is a number from 1-31, with 1 being highest quality/largest filesize and 31 being the lowest quality/smallest filesize
     */
    fun setQualityScale(quality: Int) = apply {
        if (quality in DEFAULT_QUALITY__MIN_SCALE..DEFAULT_QUALITY__MAX_SCALE) this.qualityScale = quality
    }

    /**
     * Set output file type like img, png
     */
    fun setOutputFileType(fileType: ImageType) = apply { outputFileType = fileType.invoke() }

    fun setOutputFileDirPath(dirPath: String) = apply { outputFileDirPath = dirPath }

    fun setOverlayPicPath(path: String?) = apply { overlayPicPath = path }

    /**
     * Set time to take a single frame for pattern - hh:mm:ss.ms
     * E.G. To take frame at 5 second - set 00:00:05.000
     */
    fun setSingleFragmeTakeTime(takeTime: String) = apply {
        frequency = SINGLE
        singleFrameTakeTime = takeTime
    }

    /**
     * To calculate custom fps - set frame_count/second
     * F.E. If you want take frame every five second - 1/5, every sixteen seconds - 1/16 etc.
     */
    fun setCustomFPS(frameCount: Int, perSeconds: Double) = apply {
        frequency = CUSTOM
        customFPS = calculateFps(frameCount, perSeconds)
    }

    @SuppressLint("CheckResult")
    fun execute(onSuccess: (Result) -> Unit,
                onFailure: (Throwable) -> Unit,
                onProgress: (Long) -> Unit) {
        if (inputFilePath.isNullOrEmpty() || File(inputFilePath).exists().not() || outputFileName.isNullOrEmpty()) {
            onFailure.invoke(UnsupportedOperationException("Input or output files shouldn't be empty"))
            return
        }
        if (outputFileDirPath == null) outputFileDirPath = context.cacheDir.absolutePath
        val videoOptions = getVideoOptions(inputFilePath)
        generateComplexCommand(videoOptions)

        ResultCache.get(complexCommand.joinToString())?.let {
            onSuccess.invoke(it)
        } ?: run {
            FFmpegHelper.execFFmpegBinary(complexCommand.toTypedArray(), context, videoOptions?.videoLength
                    ?: 0L)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ percents ->
                        if (percents == PERCENT_SUCCESSFULLY) {
                            proceedResult(videoOptions)?.let {
                                ResultCache.put(complexCommand.joinToString(), it)
                                onSuccess(it)
                            }
                        } else {
                            onProgress(percents)
                        }
                    }, onFailure)
        }
    }

    fun getVideoOptions(inputFile: String?): VideoOptions? =
            inputFile?.let {
                FFmpegMediaMetadataRetriever().run {
                    setDataSource(context, Uri.parse(inputFile))
                    val timeMs = extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)
                    val width = extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                    val height = extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                    val size = extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FILESIZE)
                    val format = File(inputFile).extension
                    release()
                    VideoOptions(it,
                            size.toLong(),
                            timeMs.toLong(),
                            format,
                            height.toInt(),
                            width.toInt())
                }
            }

    fun getThumbnailOptions(thumbnail: File, fpms: Long, index: Int, videoLength: Long): Thumbnail? =
            Thumbnail(thumbnail.length(),
                    thumbnail.absolutePath,
                    outputFileType,
                    (index * fpms).let { if (it > videoLength) videoLength else it },
                    BitmapFactory.decodeFile(thumbnail.path))

    private fun generateComplexCommand(videoOptions: VideoOptions?) {
        complexCommand.apply {
            clear()
            if (isOverride) add(OVERRIDE_EXISTING)

            inputFilePath?.let { addAll(listOf(INPUT_ARG, it)) }
            overlayPicPath?.let { addAll(listOf(INPUT_ARG, it)) }

            when (frequency) {
                EVERY_SECOND -> videoFilters.add(calculateFps(1, 1.0))
                EVERY_TEN_SECOND -> videoFilters.add(calculateFps(1, 10.0))
                EVERY_THIRTY_SECOND -> videoFilters.add(calculateFps(1, 30.0))
                EVERY_MINUTE -> videoFilters.add(calculateFps(1, 60.0))
                ALL_FRAMES -> videoFilters.add(SELECT_ALL_FRAMES)
                FIXED_FRAME_COUNT -> inputFilePath?.let {
                    videoFilters.add(getRateFps(outputThumbnailCount, videoOptions?.videoLength))
                }
                CUSTOM -> videoFilters.add(customFPS)
                SINGLE -> {
                    singleFrameTakeTime?.let {
                        add(START_SECONDS)
                        add(it)
                    }
                    add(VIDEO_FRAME)
                    add(SINGLE_FRAME)
                }
            }
            overlayPicPath?.let {
                videoFilters.add("$OVERLAY_PICT${overlayPicCoordinates
                    ?: DEFAULT_OVERLAY_POSITION}")
            }
            videoFilters.add("$SCALE_SET$scaleSize")
            addAll(listOf(VIDEO_FILTER, videoFilters.joinToString()))
            addAll(listOf(VIDEO_SYNC_METHOD, videoSyncMethod.invoke()))
            addAll(listOf(QUALITY_SCALE, qualityScale.toString()))
            add("$outputFileDirPath${File.separator}$outputFileName$DEFAULT_FILES_PATTERN.$outputFileType")
        }
    }

    private fun proceedResult(videoOptions: VideoOptions?): Result? = findCreatedImages()?.let { files ->
        Result(mutableListOf<Thumbnail?>().apply {
            videoOptions?.videoLength?.let {
                val resultFps = if (files.count() == 1) 0L else it / (files.count() - 1)
                files.forEachIndexed { index, file ->
                    add(getThumbnailOptions(file, resultFps, index, it))
                }
            }
        }.filterNotNull(), videoOptions)
    }

    private fun calculateFps(frameCount: Int, perSeconds: Double) = "$FPS_SET$frameCount/$perSeconds"

    private fun getRateFps(frameCount: Int, duration: Long?): String {
        val videoLength = TimeUnit.MILLISECONDS.toSeconds(duration ?: 0L)
        return calculateFps(1, if (frameCount != 1) videoLength.toDouble() / (frameCount - 1) else videoLength.toDouble())
    }

    /**
     * Used for finding created thumbnail by the specified pattern
     * e.g. fileName = example, fileType - jpg, result will be "example0001.jpg"
     */
    private fun findCreatedImages() = File(outputFileDirPath).takeIf { it.exists() && it.isDirectory }?.let {
        val pattern = Pattern.compile("$outputFileName\\d+\\.$outputFileType")
        it.listFiles { file -> pattern.matcher(file.name).matches() }
    }
}