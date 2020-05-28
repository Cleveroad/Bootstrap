package com.cleveroad.bootstrap.kotlin_gps

import android.location.Location
import androidx.lifecycle.MutableLiveData

object LocationProvider {

    /**
     * @return current Location
     */
    val locationLiveData = MutableLiveData<Location>()

    /**
     * @return true if the location is tracking at the moment
     */
    val isTrackingLD = MutableLiveData<Boolean>()
}
