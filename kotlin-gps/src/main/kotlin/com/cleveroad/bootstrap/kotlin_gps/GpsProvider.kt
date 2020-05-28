package com.cleveroad.bootstrap.kotlin_gps

import android.app.Notification
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.cleveroad.bootstrap.kotlin_gps.dialog.AllowPermissionDialog
import com.cleveroad.bootstrap.kotlin_gps.dialog.GpsConfirmDialog
import com.cleveroad.bootstrap.kotlin_gps.service.GpsTrackerService
import java.lang.ref.WeakReference

interface GpsProvider {
    val weekContext: WeakReference<Context>

    fun getContext() = weekContext.get()

    /**
     * You have to use BuildConfig.APPLICATION_ID
     */
    val applicationId: String

    /**
     * Notification for foreground service
     */
    val notificationForForegroundService: Notification

    /**
     * title/message/positiveBottom [GpsConfirmDialog] in [AlertDialog]
     */
    val messageGpsConfirmDialog: String?
    val titleGpsConfirmDialog: String?
    val positiveButtonGpsConfirmDialog: String?
    val negativeButtonGpsConfirmDialog: String?
    val isCancelableGps: Boolean

    /**
     * title/message/positiveBottom [AllowPermissionDialog] in [AlertDialog]
     */
    val messageAllowPermissionDialog: String?
    val titleAllowPermissionDialog: String?
    val positiveButtonAllowPermissionDialog: String?
    val negativeButtonAllowPermissionDialog: String?
    val isCancelablePermission: Boolean

    /**
     * Interval milliseconds - for check location
     */
    val intervalMilliseconds: Long

    /**
     * Is fastest interval milliseconds - if need do long work after update location set true
     * and min interval = 5000 milliseconds
     */
    val isFastestInterval: Boolean

    /**
     * Minimum distance between location updates, in meters
     */
    val smallestDisplacement: Float

    /**
     * PRIORITY_HIGH_ACCURACY = 100
     * PRIORITY_BALANCED_POWER_ACCURACY = 102
     * PRIORITY_LOW_POWER = 104
     * PRIORITY_NO_POWER = 105
     */
    val priorityLocation: Int

    /**
     * startGpsTracker or stopGpsTrackerService
     */
    fun switchGpsTracker(isTrackLocation: Boolean) {
        if (isTrackLocation) startGpsTracker() else stopGpsTracker()
    }

    private fun stopGpsTracker() {
        weekContext.get()?.let { GpsTrackerService.stopService(it) }
    }

    private fun startGpsTracker() {
        weekContext.get()?.let {
            GpsTrackerService.startTracking(it,
                    notificationForForegroundService,
                    intervalMilliseconds,
                    priorityLocation,
                    isFastestInterval,
                    smallestDisplacement)
        }
    }

    /**
     * if you want do something before [GpsConfirmDialog] override this method
     *
     * @return true method will not be called [GpsConfirmDialog]
     * or false method will be called [GpsConfirmDialog]
     */
    fun showDialogGps() = false

    /**
     * if you want do something before [AllowPermissionDialog] override this method
     *
     * @return true method will not be called [AllowPermissionDialog]
     * or false method will be called [AllowPermissionDialog]
     */
    fun showDialogPermission() = false
}