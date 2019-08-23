package com.cleveroad.bootstrap.kotlin_core.utils.storage

enum class TypeMediaFile(val value: String) {
    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audio"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun byValue(value: String?) =
                values().firstOrNull { value == it.value } ?: UNKNOWN
    }
}