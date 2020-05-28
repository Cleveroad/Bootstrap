package com.cleveroad.bootstrap.kotlin_auth.linkedin.client

import android.app.Activity
import java.util.concurrent.atomic.AtomicReference

class LinkedInAuthState {

    private val handlerRef = AtomicReference<AuthHandler>(null)

    val handler
        get() = handlerRef.get()

    fun beginAuthorize(activity: Activity, handler: AuthHandler) =
            handler.authorize(activity).also {
                handlerRef.set(handler)
            }
}