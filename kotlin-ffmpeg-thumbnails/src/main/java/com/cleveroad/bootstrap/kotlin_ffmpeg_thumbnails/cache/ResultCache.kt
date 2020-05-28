package com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.cache

import android.util.LruCache
import com.cleveroad.bootstrap.kotlin_ffmpeg_thumbnails.model.Result

object ResultCache {

    private const val CACHE_SIZE = 16

    private val lruCache = LruCache<String, Result>(CACHE_SIZE)

    fun get(command: String): Result? = lruCache.get(command)

    fun put(command: String, result: Result) = lruCache.put(command, result)
}