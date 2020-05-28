package com.cleveroad.gps_example

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.cleveroad.bootstrap.kotlin.BuildConfig
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_gps.GpsProviderImpl
import com.cleveroad.bootstrap.kotlin_gps.GpsUiDelegate
import com.cleveroad.bootstrap.kotlin_gps.GpsUiDelegateImpl
import com.cleveroad.bootstrap.kotlin_gps.LocationProvider
import kotlinx.android.synthetic.main.activity_gps.*

class GpsActivity : AppCompatActivity(), GpsUiDelegate.GpsUiCallback {

    companion object {
        fun start(context: Context) = context.run {
            startActivity(Intent(this, GpsActivity::class.java))
        }
    }

    private val notification by lazy {
        NotificationUtils(this).createNotification()
    }

    private val gpsProvider by lazy {
        GpsProviderImpl.Builder(BuildConfig.APPLICATION_ID, this, notification).apply {
            dialogCallback = object : GpsProviderImpl.DialogCallback {
                override fun showDialogGps(): Boolean {
                    Toast.makeText(this@GpsActivity, "Gps", Toast.LENGTH_LONG).show()
                    return false
                }

                override fun showDialogPermission(): Boolean {
                    Toast.makeText(this@GpsActivity, "permission", Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }.build()
    }

    private val gpsUiDelegate by lazy {
        GpsUiDelegateImpl(this, gpsProvider)
    }

    private val locationObserver = Observer<Location?> { location ->
        location?.run { tvText.text = "$latitude $longitude" }
    }

    private val locationIsTracking = Observer<Boolean?> { isTracking ->
        isTracking?.let { tvIsTracking.text = it.toString() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)
        LocationProvider.run {
            locationLiveData.observe(this@GpsActivity, locationObserver)
            isTrackingLD.observe(this@GpsActivity, locationIsTracking)
        }
    }

    override fun getActivity(): AppCompatActivity = this

    override fun onPause() {
        super.onPause()
        gpsUiDelegate.onPause()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        gpsUiDelegate.onResumeFragments()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        gpsUiDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        gpsUiDelegate.onDestroy()
        super.onDestroy()
    }
}