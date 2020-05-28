package com.cleveroad.bootstrap.kotlin_gps.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.location.LocationManager.NETWORK_PROVIDER
import java.lang.ref.WeakReference

class GpsProviderReceiver(private val locationManager: LocationManager,
                          callback: GpsCallback) : BroadcastReceiver() {

    private var gpsCallbackWeakReference = WeakReference<GpsCallback>(callback)

    override fun onReceive(context: Context, intent: Intent) {
        gpsCallbackWeakReference.get()?.apply {
            locationManager.run {
                gpsStateChanged(isProviderEnabled(GPS_PROVIDER)
                        && isProviderEnabled(NETWORK_PROVIDER))
            }
        }
    }

    @FunctionalInterface
    interface GpsCallback {
        fun gpsStateChanged(enabled: Boolean)
    }
}