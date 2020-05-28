package com.cleveroad.bootstrap.kotlin_gps

import androidx.appcompat.app.AppCompatActivity


interface GpsUiDelegate {

    fun onResumeFragments()

    fun onPause()

    fun onDestroy()

    fun checkPermission()

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)

    fun showConfirmDialog()

    interface GpsUiCallback {
        fun getActivity(): AppCompatActivity
    }
}