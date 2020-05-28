package com.cleveroad.bootstrap.kotlin_gps

import android.location.Location


internal object LocationProcessorImpl : LocationProcessor {

    override fun locationUpdated(location: Location) {
        LocationProvider.locationLiveData.value = location
    }

    override fun close() {
        LocationProvider.locationLiveData.value = null
    }
}