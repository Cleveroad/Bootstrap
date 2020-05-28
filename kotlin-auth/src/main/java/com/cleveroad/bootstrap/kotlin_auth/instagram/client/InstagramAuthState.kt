package com.cleveroad.bootstrap.kotlin_auth.instagram.client

import android.app.Activity
import java.util.concurrent.atomic.AtomicReference

class InstagramAuthState {

    private val handlerRef = AtomicReference<AuthHandler>(null)

    val handler : AuthHandler
        get() = handlerRef.get()

    fun beginAuthorize(activity: Activity, handler: AuthHandler) =
            handler.authorize(activity).also {
                handlerRef.set(handler)
            }
}