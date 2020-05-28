package com.cleveroad.bootstrap.kotlin_gps

import android.content.Context
import android.content.IntentFilter
import android.location.LocationManager
import com.cleveroad.bootstrap.kotlin_gps.receiver.GpsProviderReceiver


internal class GpsProviderTrackerImpl(private val gpsUiDelegate: GpsUiDelegate,
                                      private val gpsProvider: GpsProvider) : GpsProviderTracker,
        GpsProviderReceiver.GpsCallback {

    private val locationManager by lazy { gpsProvider.getContext()?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager }

    private var gpsProviderReceiver: GpsProviderReceiver? = null

    override fun startTrack() = gpsProvider.run {
        if (gpsProviderReceiver == null) {
            locationManager?.let { gpsProviderReceiver = GpsProviderReceiver(it, this@GpsProviderTrackerImpl) }
            getContext()?.registerReceiver(gpsProviderReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
        }
        switchGpsTracker(isGpsOn())
    }

    override fun stopTrack() = gpsProvider.run {
        gpsProviderReceiver?.let {
            getContext()?.unregisterReceiver(it)
            gpsProviderReceiver = null
        }
        switchGpsTracker(false)
    }

    override fun isGpsOn() = locationManager?.run {
        isProviderEnabled(LocationManager.GPS_PROVIDER)
                || isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    } ?: false

    override fun gpsStateChanged(enabled: Boolean) = gpsUiDelegate.run {
        if (enabled) {
            checkPermission()
        } else {
            gpsProvider.switchGpsTracker(false)
            showConfirmDialog()
        }
    }
}