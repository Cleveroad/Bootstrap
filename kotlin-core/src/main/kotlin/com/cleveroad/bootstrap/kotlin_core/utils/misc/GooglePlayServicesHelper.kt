package com.cleveroad.bootstrap.kotlin_core.utils.misc


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

/**
 * Utils for detecting if Google Play Services is installed and up to date
 */
class GooglePlayServicesHelper {

    private var callback: IGooglePlayServicesCallback? = DEFAULT
    var requestCode = GOOGLE_PLAY_SERVICES_REQUEST_CODE

    companion object {

        private const val GOOGLE_PLAY_SERVICES_REQUEST_CODE = 0x1234

        private val DEFAULT = object : IGooglePlayServicesCallback {
            override fun onGooglePlayServicesAvailable() = Unit
            override fun onGooglePlayServicesNotAvailable(resultCode: Int) = Unit
            override fun onGooglePlayServicesUpdateCancelled() = Unit
        }
    }

    fun setCallback(callback: IGooglePlayServicesCallback?): GooglePlayServicesHelper = apply {
        this.callback = callback ?: DEFAULT
    }

    /**
     * Call this method in activity's onCreate
     *
     * @param context context
     */
    fun onCreate(context: Context) {
        callback?.apply {
            val result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
            when (result) {
                ConnectionResult.SUCCESS -> onGooglePlayServicesAvailable()
                else -> onGooglePlayServicesNotAvailable(result)
            }
        }
    }

    /**
     * Call this method in activity's onActivityResult
     *
     * @param context     context
     * @param requestCode request code
     * @param resultCode  result code
     */
    fun onActivityResult(context: Context,
                         requestCode: Int,
                         resultCode: Int) {
        if (requestCode == this.requestCode) {
            callback?.apply {
                when (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)) {
                    ConnectionResult.SUCCESS -> onGooglePlayServicesAvailable()
                    else -> onGooglePlayServicesUpdateCancelled()
                }
            }
        }
    }

    /**
     * Show Google Play Services error dialog
     *
     * @param activity   hosted activity
     * @param resultCode error code from Google Play Services
     */
    fun showErrorDialog(activity: AppCompatActivity,
                        resultCode: Int) {
        GoogleApiAvailability.getInstance()
                .getErrorDialog(activity, resultCode, requestCode)?.show()
    }

    interface IGooglePlayServicesCallback {
        /**
         * This method will be called if GPS are ready to use
         */
        fun onGooglePlayServicesAvailable()

        /**
         * This method will be called if there are any errors found.
         *
         * @param resultCode error code from Google Play Services
         */
        fun onGooglePlayServicesNotAvailable(resultCode: Int)

        /**
         * This method will be called if user cancels GPS update process
         */
        fun onGooglePlayServicesUpdateCancelled()
    }
}
