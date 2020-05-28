package com.cleveroad.bootstrap.kotlin_gps.service

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.util.Log
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import com.cleveroad.bootstrap.kotlin_gps.LocationProcessor
import com.cleveroad.bootstrap.kotlin_gps.LocationProcessorImpl
import com.cleveroad.bootstrap.kotlin_gps.LocationProvider
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class GpsTrackerService : LifecycleService(),
        LifecycleOwner,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private const val SERVICE_ID = 666

        // 5 minutes
        private const val LOCATION_INTERVAL_DEFAULT = 1000L * 60L * 5L
        //1 minute
        private const val LOCATION_INTERVAL_MINIMUM = 1000L * 5L
        //Minimum distance between location updates, in meters
        private const val SMALLEST_DISPLACEMENT = 10F

        private val TAG = GpsTrackerService::class.java.simpleName
        private val EXTRA_IS_ON = "is_on$TAG"
        private val EXTRA_INTERVAL = "interval$TAG"
        private val EXTRA_PRIORITY = "priority$TAG"
        private val EXTRA_DISPLACEMENT = "displacement$TAG"
        private val EXTRA_USE_FASTEST_INTERVAL = "fastest$TAG"

        private var notificationForForeground: Notification? = null

        /**
         * Start gps tracking service
         *
         * @param context - [Context] instance
         * @param notification [Notification] instance. Set null if you do not need to start service as foreground
         * @param locationInterval Location detection interval
         * @param priorityLocation Must be:
         * [PRIORITY_HIGH_ACCURACY],
         * [PRIORITY_BALANCED_POWER_ACCURACY],
         * [PRIORITY_LOW_POWER],
         * [PRIORITY_NO_POWER]
         *
         * @param isFastestInterval Location detection fastest interval
         *
         */
        fun startTracking(context: Context,
                          notification: Notification?,
                          locationInterval: Long = LOCATION_INTERVAL_DEFAULT,
                          priorityLocation: Int = PRIORITY_HIGH_ACCURACY,
                          isFastestInterval: Boolean = false,
                          smallestDisplacement: Float = SMALLEST_DISPLACEMENT) {
            this.notificationForForeground = notification
            val intent = createIntent(context, locationInterval, priorityLocation, smallestDisplacement, isFastestInterval)

            with(context) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notification != null) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
            }
        }

        /**
         * Start gps tracking as bound service
         *
         * @param activity Activity, which you want bind service to
         * @param locationInterval Location detection interval
         * @param priorityLocation Must be:
         * [PRIORITY_HIGH_ACCURACY],
         * [PRIORITY_BALANCED_POWER_ACCURACY],
         * [PRIORITY_LOW_POWER],
         * [PRIORITY_NO_POWER]
         *
         * @param isFastestInterval Location detection fastest interval
         *
         */
        fun startBoundTracking(activity: Activity,
                               connection: ServiceConnection,
                               locationInterval: Long = LOCATION_INTERVAL_DEFAULT,
                               priorityLocation: Int = PRIORITY_HIGH_ACCURACY,
                               isFastestInterval: Boolean = false,
                               smallestDisplacement: Float = SMALLEST_DISPLACEMENT) {
            val intent = createIntent(activity, locationInterval, priorityLocation, smallestDisplacement, isFastestInterval)
            activity.bindService(intent, connection, Service.BIND_AUTO_CREATE)
        }

        /**
         * Stop gps tracking
         *
         * @param context - [Context]
         */
        fun stopTracking(context: Context) {
            context.startService(Intent(context, GpsTrackerService::class.java)
                    .apply { putExtra(EXTRA_IS_ON, TrackerState.OFF) })
            notificationForForeground = null
        }

        /**
         * Stop gps service
         *
         * @param context - [Context]
         */
        fun stopService(context: Context) {
            context.stopService(Intent(context, GpsTrackerService::class.java))
            notificationForForeground = null
        }

        private fun createIntent(ctx: Context,
                                 locationInterval: Long,
                                 priorityLocation: Int,
                                 smallestDisplacement: Float,
                                 isFastestInterval: Boolean) =
                Intent(ctx, GpsTrackerService::class.java)
                        .putExtra(EXTRA_IS_ON, TrackerState.ON)
                        .putExtra(EXTRA_INTERVAL, locationInterval)
                        .putExtra(EXTRA_PRIORITY, priorityLocation)
                        .putExtra(EXTRA_DISPLACEMENT, smallestDisplacement)
                        .putExtra(EXTRA_USE_FASTEST_INTERVAL, isFastestInterval)
    }

    private var locationInterval = LOCATION_INTERVAL_DEFAULT
    private var isFastestInterval = false
    private var priorityLocation = PRIORITY_HIGH_ACCURACY
    private var smallestDisplacement = SMALLEST_DISPLACEMENT

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult?) {
            location?.let { currentLocation ->
                locationProcessor?.locationUpdated(currentLocation.lastLocation)
            }
        }
    }

    private val locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            locationProcessor?.locationUpdated(location)
        }

        override fun onProviderDisabled(provider: String) {
            LocationProvider.isTrackingLD.postValue(false)
        }

        override fun onProviderEnabled(provider: String) {
            LocationProvider.isTrackingLD.postValue(true)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) = Unit
    }

    private var locationProcessor: LocationProcessor? = null

    private val binder = GpsTrackerBinder()

    private val apiClient by lazy {
        GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    private val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    }

    override fun onCreate() {
        startForeground()
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        super.onBind(intent)
        getExtras(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = intent.let {
        getExtras(it)
        when (it?.getSerializableExtra(EXTRA_IS_ON)) {
            TrackerState.ON -> {
                connect()
                Service.START_STICKY
            }
            TrackerState.OFF -> {
                disconnect()
                super.onStartCommand(it, flags, startId)
            }
            else -> super.onStartCommand(it, flags, startId)
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        disconnect()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        disconnect()
        super.onDestroy()
    }

    override fun onConnected(bundle: Bundle?) {
        startGMSLocationUpdates()
    }

    override fun onConnectionSuspended(i: Int) {
        apiClient.reconnect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        connectionResult.errorMessage?.takeIf { it.isNotEmpty() }?.let { Log.e(TAG, it) }
        LocationProvider.isTrackingLD.postValue(false)
    }

    /**
     * Start location tracking
     */
    fun connect() {
        if (checkPlayServices()) connectGoogleApiClient() else startAndroidLocationUpdates()
    }

    /**
     * Stop location tracking and release resources
     */
    fun disconnect() {
        locationProcessor?.let {
            it.close()
            locationProcessor = null
        }
        locationManager?.removeUpdates(locationListener)
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback)
        apiClient.disconnect()
        LocationProvider.isTrackingLD.postValue(false)
        stopForeground(true)
    }

    private fun getExtras(intent: Intent?) = intent?.run {
        locationInterval = Math.max(getLongExtra(EXTRA_INTERVAL, locationInterval), LOCATION_INTERVAL_MINIMUM)
        priorityLocation = getIntExtra(EXTRA_PRIORITY, priorityLocation)
        isFastestInterval = getBooleanExtra(EXTRA_USE_FASTEST_INTERVAL, isFastestInterval)
        smallestDisplacement = getFloatExtra(EXTRA_DISPLACEMENT, smallestDisplacement)
    }

    private fun startForeground() {
        notificationForForeground?.let {
            startForeground(SERVICE_ID, it)
        }
    }

    private fun connectGoogleApiClient() {
        if (checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
                && checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
            with(apiClient) {
                if (!isConnected && !isConnecting) connect() else reconnect()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startGMSLocationUpdates() {
        if (checkLocationPermissions()) {
            locationProcessor = LocationProcessorImpl
            LocationServices
                    .getFusedLocationProviderClient(this)
                    .requestLocationUpdates(createRequest(), locationCallback, Looper.myLooper())
            LocationProvider.isTrackingLD.postValue(true)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startAndroidLocationUpdates() {
        if (checkLocationPermissions()) {
            locationProcessor = LocationProcessorImpl
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER.takeIf {
                locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
            } ?: LocationManager.GPS_PROVIDER,
                    locationInterval, smallestDisplacement, locationListener)
            LocationProvider.isTrackingLD.postValue(true)
        }
    }

    private fun createRequest() = LocationRequest().apply {
        priority = priorityLocation
        if (isFastestInterval) {
            fastestInterval = locationInterval
        } else {
            interval = locationInterval
        }
        smallestDisplacement = this@GpsTrackerService.smallestDisplacement
    }

    private fun checkPlayServices() = GoogleApiAvailability.getInstance().run {
        isGooglePlayServicesAvailable(this@GpsTrackerService) == ConnectionResult.SUCCESS
    }

    private fun checkLocationPermissions() = checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
            && (checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED)

    inner class GpsTrackerBinder : Binder() {
        fun getService() = this@GpsTrackerService
    }
}