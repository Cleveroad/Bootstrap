package com.cleveroad.gps_example

import android.app.Notification
import android.app.Notification.VISIBILITY_PRIVATE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.NotificationManager.IMPORTANCE_NONE
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.cleveroad.bootstrap.kotlin.R

class NotificationUtils(val context: Context) {

    companion object {
        private const val EMPTY_STRING = ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): String {
        val channelId = "${context.getString(R.string.app_name)} location"
        NotificationChannel(channelId,
                channelId, IMPORTANCE_HIGH).apply {
            importance = IMPORTANCE_NONE
            lockscreenVisibility = VISIBILITY_PRIVATE
            (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(this)
        }
        return channelId
    }

    /**
     * Example create notification for foreground service
     */
    @SuppressWarnings("unused")
    fun createNotification(): Notification {
        var channelId = EMPTY_STRING
        if (SDK_INT >= O) channelId = createNotificationChannel()
        return NotificationCompat.Builder(context, channelId).run {
            setOngoing(true)
            setSmallIcon(R.mipmap.ic_launcher)
            priority = NotificationCompat.PRIORITY_MIN
            setContentText(context.getString(R.string.track_location_notification))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setCategory(Notification.CATEGORY_SERVICE)
            }
            setContentIntent(getActivity(context, 0,
                    Intent(context, GpsActivity::class.java), 0))
            build()
        }
    }
}

