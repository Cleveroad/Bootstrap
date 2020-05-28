package com.cleveroad.bootstrap.kotlin_auth.twitter.client.utils

import android.content.Context
import android.content.Intent

private const val NO_FLAGS = 0

fun Intent.isActivityAvailable(context: Context) =
    context.packageManager?.queryIntentActivities(this, NO_FLAGS)?.isNotEmpty() ?: false