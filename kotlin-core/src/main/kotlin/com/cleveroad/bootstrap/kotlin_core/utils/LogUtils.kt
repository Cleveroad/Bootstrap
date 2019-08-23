package com.cleveroad.bootstrap.kotlin_core.utils

import android.os.Build
import android.util.Log
import com.cleveroad.bootstrap.kotlin_core.BuildConfig
import java.util.regex.Pattern

object Logger {

    private const val CALL_STACK_INDEX = 2

    private const val MAX_TAG_LENGTH = 23

    private const val PATTERN = "(\\$\\d+)+$"

    private var url = ""

    fun v(exception: Exception? = null, callStackLevel: Int = 0, tag: String = callerTag(callStackLevel), message: () -> String) = inDebug {
        Log.v(tag, message() + url, exception)
    }

    fun d(exception: Exception? = null, callStackLevel: Int = 0, tag: String = callerTag(callStackLevel), message: () -> String) = inDebug {
        Log.d(tag, message() + url, exception)
    }

    fun i(exception: Exception? = null, callStackLevel: Int = 0, tag: String = callerTag(callStackLevel), message: () -> String) = inDebug {
        Log.i(tag, message() + url, exception)
    }

    fun w(exception: Exception? = null, callStackLevel: Int = 0, tag: String = callerTag(callStackLevel), message: () -> String) = inDebug {
        Log.w(tag, message() + url, exception)
    }

    fun e(exception: Exception? = null, callStackLevel: Int = 0, tag: String = callerTag(callStackLevel), message: () -> String) = inDebug {
        Log.e(tag, message() + url, exception)
    }

    fun wtf(exception: Exception? = null, callStackLevel: Int = 0, tag: String = callerTag(callStackLevel), message: () -> String) = inDebug {
        Log.wtf(tag, message() + url, exception)
    }

    private inline fun inDebug(action: () -> Unit) {
        if (BuildConfig.DEBUG) {
            action()
        }
    }

    /**
     * @return The class name for the calling class as a String.
     */
    private fun callerTag(callStackLevel: Int = 0): String {
        var tag: String
        var callStackLevelLocale = callStackLevel
        do {
            val stackTrace = Throwable().stackTrace
            tag = stackTrace[CALL_STACK_INDEX + callStackLevelLocale].className
            val matcher = Pattern.compile(PATTERN).matcher(tag)

            if (matcher.find()) tag = matcher.replaceAll("")

            tag = tag.substring(tag.lastIndexOf('.') + 1)

            url = "(${stackTrace[CALL_STACK_INDEX + callStackLevelLocale].fileName}:${stackTrace[CALL_STACK_INDEX + callStackLevelLocale].lineNumber})"
            callStackLevelLocale++
        } while (url.contains("AnyExtensions"))
        // Tag length limit was removed in API 24.
        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tag
        } else {
            tag.substring(0, MAX_TAG_LENGTH)
        }
    }
}

/**
 * Print log error
 *
 * @param text [String] It`s text to print in logs
 * @param callLevel [Int] This is the level from which starts print exception tree
 *
 * @return [T] return the object from with it method was called
 */
fun <T> T?.printLogE(text: String? = "", callLevel: Int = 1) = apply {
    Logger.run {
        e(message = { "$text ${this@printLogE}" }, callStackLevel = callLevel)
    }
}

/**
 * Print log debug
 *
 * @param text [String] It`s text to print in logs
 * @param callLevel [Int] This is the level from which starts print exception tree
 *
 * @return [T] return the object from with it method was called
 */
fun <T> T?.printLog(text: String? = "", callLevel: Int = 1) = apply {
    Logger.run {
        d(message = { "$text ${this@printLog}" }, callStackLevel = callLevel)
    }
}