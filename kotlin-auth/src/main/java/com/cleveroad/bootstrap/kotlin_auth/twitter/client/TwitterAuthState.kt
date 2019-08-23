package com.cleveroad.bootstrap.kotlin_auth.twitter.client

import android.app.Activity
import java.util.concurrent.atomic.AtomicReference

class TwitterAuthState {

    private val handlerRef = AtomicReference<AuthHandler>(null)

    val handler
        get() = handlerRef.get()

    fun beginAuthorize(activity: Activity, handler: AuthHandler): Boolean {
        handlerRef.set(handler)
        return handler.authorize(activity)
    }
}