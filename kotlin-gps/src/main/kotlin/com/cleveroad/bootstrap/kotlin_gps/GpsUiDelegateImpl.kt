package com.cleveroad.bootstrap.kotlin_gps

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.DialogFragment
import com.cleveroad.bootstrap.kotlin_gps.dialog.AllowPermissionDialog
import com.cleveroad.bootstrap.kotlin_gps.dialog.GpsConfirmDialog


class GpsUiDelegateImpl(private val gpsUiCallback: GpsUiDelegate.GpsUiCallback,
                        private val gpsProvider: GpsProvider) : GpsUiDelegate {

    companion object {
        private const val GEO_POSITION_REQUEST_CODE = 10001
    }

    private val gpsProviderTracker by lazy(LazyThreadSafetyMode.NONE) {
        GpsProviderTrackerImpl(this, gpsProvider)
    }

    private var resumed = false

    private var isDeniedBySystem = false

    override fun onResumeFragments() {
        resumed = true
        if (isDeniedBySystem) {
            showPermissionDialog()
            isDeniedBySystem = false
            return
        }
        if (gpsUiCallback.getActivity().supportFragmentManager.findFragmentByTag(AllowPermissionDialog::class.java.simpleName) == null) {
            checkPermission()
        }
    }

    override fun onPause() {
        resumed = false
    }

    override fun onDestroy() {
        gpsProviderTracker.stopTrack()
    }

    override fun checkPermission() {
        if (checkSelfPermission(gpsUiCallback.getActivity(),
                        ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
                && checkSelfPermission(gpsUiCallback.getActivity(),
                        ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {

            requestPermissions(gpsUiCallback.getActivity(),
                    arrayOf(ACCESS_FINE_LOCATION), GEO_POSITION_REQUEST_CODE)
        } else {
            if (gpsProviderTracker.isGpsOn()) {
                gpsProviderTracker.startTrack()
            } else {
                showConfirmDialog()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            GEO_POSITION_REQUEST_CODE -> if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                gpsProviderTracker.startTrack()
            } else {
                isDeniedBySystem = true
            }
        }
    }

    private fun showPermissionDialog() {
        if (resumed) {
            gpsUiCallback.getActivity()
                    .supportFragmentManager
                    .findFragmentByTag(AllowPermissionDialog::class.java.name)
                    ?: gpsProvider.run {
                        if (showDialogPermission().not()) {
                            AllowPermissionDialog.newInstance(applicationId,
                                    titleAllowPermissionDialog,
                                    messageAllowPermissionDialog,
                                    positiveButtonAllowPermissionDialog,
                                    negativeButtonAllowPermissionDialog,
                                    isCancelablePermission)
                                    .show(gpsUiCallback.getActivity().supportFragmentManager,
                                            AllowPermissionDialog::class.java.simpleName)
                        }
                    }
        }
    }

    override fun showConfirmDialog() {
        if (resumed) {
            gpsUiCallback.getActivity()
                    .supportFragmentManager
                    .findFragmentByTag(GpsConfirmDialog::class.java.name) as? DialogFragment
                    ?: gpsProvider.run {
                        if (showDialogGps().not()) {
                            GpsConfirmDialog.newInstance(messageGpsConfirmDialog,
                                    titleGpsConfirmDialog,
                                    positiveButtonGpsConfirmDialog,
                                    negativeButtonGpsConfirmDialog,
                                    isCancelableGps)
                                    .show(gpsUiCallback.getActivity().supportFragmentManager,
                                            GpsConfirmDialog::class.java.name)
                        }
                    }
        }
    }
}