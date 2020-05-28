package com.cleveroad.bootstrap.kotlin_ext

fun<T> List<T>.forEachExcluding(item: T, action: (T) -> Unit) = forEach { if (it !== item) action(it) }