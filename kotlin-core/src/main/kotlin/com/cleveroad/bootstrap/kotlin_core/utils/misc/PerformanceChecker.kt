package com.cleveroad.bootstrap.kotlin_core.utils.misc

import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Simple performance checker.
 */
object PerformanceChecker {

    private const val MICROSECONDS_IN_MILLISECOND = 1000
    private const val MILLISECONDS_IN_SECOND = 1000
    private const val TIME_PATTERN = "%.3f ms"

    private val performanceInfoMap = HashMap<String, PerformanceInfo>()

    /**
     * Call this method to save beginning time by passed key
     * @param key value to identify calculation for performance
     */
    fun start(key: String) =
            performanceInfoMap.put(key, PerformanceInfo().apply { setStartTime(System.nanoTime()) })

    /**
     * Call this method to get calculated PerformanceInfo by passed key
     * @param key value to identify calculation for performance
     * @return calculate PerformanceInfo for passed key
     */
    fun stop(key: String): PerformanceInfo? =
            performanceInfoMap.remove(key)?.apply { setEndTime(System.nanoTime()) }

    class PerformanceInfo {

        private var startTime: Long = 0
        private var endTime: Long = 0

        internal fun setStartTime(startTime: Long) {
            this.startTime = startTime
        }

        internal fun setEndTime(endTime: Long) {
            this.endTime = endTime
        }

        override fun toString(): String {
            if (startTime <= 0) {
                return "Not Started"
            }
            if (endTime <= 0) {
                return "In Progress"
            }
            val mcs = TimeUnit.MICROSECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS).toFloat()
            if (mcs >= MICROSECONDS_IN_MILLISECOND) {
                var ms = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS).toFloat()
                if (ms >= MILLISECONDS_IN_SECOND) {
                    var s = TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS).toFloat()
                    s += (ms - s * MILLISECONDS_IN_SECOND) / MILLISECONDS_IN_SECOND
                    return String.format(Locale.US, TIME_PATTERN, s)
                }
                ms += (mcs - ms * MICROSECONDS_IN_MILLISECOND) / MILLISECONDS_IN_SECOND
                return String.format(Locale.US, TIME_PATTERN, ms)
            }
            return String.format(Locale.US, TIME_PATTERN, mcs / MICROSECONDS_IN_MILLISECOND)
        }
    }
}
