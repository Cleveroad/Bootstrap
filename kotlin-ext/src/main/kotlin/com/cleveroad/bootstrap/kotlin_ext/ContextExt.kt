package com.cleveroad.bootstrap.kotlin_ext

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * @return a font Typeface associated with a particular resource ID
 * or throws NotFoundException if the given ID does not exist
 *
 * @param id The desired resource identifier.
 */
fun Context.getFontResourcesCompat(@FontRes id: Int) = ResourcesCompat.getFont(this, id)

/**
 * @return a drawable object associated with a particular resource ID
 * or throws NotFoundException if the given ID does not exist
 *
 * @param id The desired resource identifier.
 */
fun Context.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(applicationContext, id)

/**
 * @return a color object associated with a particular resource ID
 * or throws NotFoundException if the given ID does not exist
 *
 * @param id The desired resource identifier.
 */
fun Context.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(applicationContext, id)

/**
 * @return a integer object associated with a particular resource ID
 * or throws NotFoundException if the given ID does not exist0
 *
 * @param id The desired resource identifier.
 */
fun Context.getInteger(@IntegerRes id: Int) = this.resources.getInteger(id)

/**
 * @return the string array object associated with a particular resource ID
 * or throws NotFoundException if the given ID does not exist
 *
 * @param id The desired resource identifier.
 */
fun Context.getStringArray(@ArrayRes id: Int) = resources.getStringArray(id)

/**
 * @return true if network connectivity exists, false otherwise.
 */
fun Context.isNetworkConnected() = withNotNull(getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager) {
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        val wifi = getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile = getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        wifi.isConnected || mobile.isConnected
    } else {
        var connected = false
        allNetworks
                .asSequence()
                .map { network -> getNetworkInfo(network) }
                .forEach { networkInfo ->
                    connected = connected or networkInfo.isConnected
                }
        connected
    }
} ?: false
