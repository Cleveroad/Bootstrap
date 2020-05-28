package com.cleveroad.progress_upload_example.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cleveroad.ExampleApp
import com.cleveroad.bootstrap.kotlin.R

class NotificationUtils {

    companion object {
        private const val NOTIFICATION_CHANNEL_NAME = "channel_upload_image_cr"
        private const val NOTIFICATION_CHANNEL_ID = "channel_upload_image_cr_id"
        private const val NOTIFICATION_ID = 89232
        private const val MAX_PROGRESS = 100
    }

    private val notificationManager by lazy {
        with(ExampleApp.instance) {
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        }
    }

    private val notificationBuilder by lazy {
        with(ExampleApp.instance) {
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_forward_black_24dp)
                .setContentTitle(resources.getString(R.string.image_upload))
                .setContentText(resources.getString(R.string.status_preparing)).apply {
                    notificationManager?.notify(NOTIFICATION_ID, this.build())
                }
                .setOngoing(true)
        }
    }

    fun showNotification(progress: Int) {
        createNotificationChannel()
        with(ExampleApp.instance.resources) {
            notificationManager?.apply {
                notificationBuilder
                    .setSmallIcon(R.drawable.ic_forward_black_24dp)
                    .setContentTitle(getString(R.string.image_upload))
                    .setContentText(getString(R.string.status_processing))
                    .setProgress(MAX_PROGRESS, progress, false)
                notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        }
    }

    fun updateSuccessStatus() {
        createNotificationChannel()
        with(ExampleApp.instance.resources) {
            notificationManager?.apply {
                notificationBuilder
                    .setSmallIcon(R.drawable.ic_done_black_24dp)
                    .setContentText(getString(R.string.status_successful))
                    .setOngoing(false)
                notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        }
    }

    fun updateErrorStatus() {
        createNotificationChannel()
        with(ExampleApp.instance.resources) {
            notificationManager?.apply {
                notificationBuilder
                    .setSmallIcon(R.drawable.ic_error_outline_black_24dp)
                    .setContentText(getString(R.string.status_failed))
                    .setOngoing(false)
                notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                notificationManager?.createNotificationChannel(this)
            }
        }
    }
}