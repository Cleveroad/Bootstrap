package com.cleveroad.bootstrap.kotlin_gps

import android.location.Location


internal interface LocationProcessor {

    /**
     * Call when location updated
     * @param location - Instance of [Location]
     */
    fun locationUpdated(location: Location)

    /**
     * Close processing
     */
    fun close()
}