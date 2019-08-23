package com.cleveroad.bootstrap.kotlin_gps

import android.app.Notification
import android.content.Context
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import java.lang.ref.WeakReference

class GpsProviderImpl : GpsProvider {

    companion object {

        //1 minute
        private const val LOCATION_INTERVAL_MILLISECONDS_DEFAULT = 1000L * 60

        inline fun build(applicationId: String,
                         context: Context,
                         notification: Notification,
                         block: Builder.() -> Unit) =
                Builder(applicationId,
                        context,
                        notification).apply(block).build()
    }

    private constructor(builder: Builder) :
            this(builder.applicationId,
                    builder.context,
                    builder.notification,
                    builder.titleGpsConfirmDialog,
                    builder.messageGpsConfirmDialog,
                    builder.positiveButtonGpsConfirmDialog,
                    builder.negativeButtonGpsConfirmDialog,
                    builder.isCancelableGps,
                    builder.titleAllowPermissionDialog,
                    builder.messageAllowPermissionDialog,
                    builder.positiveButtonAllowPermissionDialog,
                    builder.negativeButtonAllowPermissionDialog,
                    builder.isCancelablePermission,
                    builder.intervalMilliseconds,
                    builder.isFastestInterval,
                    builder.priorityLocation,
                    builder.dialogCallback)

    private constructor(applicationId: String,
                        context: Context,
                        notification: Notification,
                        titleGpsConfirmDialog: String?,
                        messageGpsConfirmDialog: String?,
                        positiveButtonGpsConfirmDialog: String?,
                        negativeButtonGpsConfirmDialog: String?,
                        isCancelableGps: Boolean,
                        titleAllowPermissionDialog: String?,
                        messageAllowPermissionDialog: String?,
                        positiveButtonAllowPermissionDialog: String?,
                        negativeButtonAllowPermissionDialog: String?,
                        isCancelablePermission: Boolean,
                        intervalMilliseconds: Long,
                        isFastestInterval: Boolean,
                        priorityLocation: Int,
                        dialogCallback: DialogCallback?) {
        this.applicationId = applicationId
        this.weekContext = WeakReference(context)
        this.titleGpsConfirmDialog = titleGpsConfirmDialog
        this.messageGpsConfirmDialog = messageGpsConfirmDialog
        this.positiveButtonGpsConfirmDialog = positiveButtonGpsConfirmDialog
        this.negativeButtonGpsConfirmDialog = negativeButtonGpsConfirmDialog
        this.isCancelableGps = isCancelableGps
        this.titleAllowPermissionDialog = titleAllowPermissionDialog
        this.messageAllowPermissionDialog = messageAllowPermissionDialog
        this.positiveButtonAllowPermissionDialog = positiveButtonAllowPermissionDialog
        this.negativeButtonAllowPermissionDialog = negativeButtonAllowPermissionDialog
        this.isCancelablePermission = isCancelablePermission
        this.intervalMilliseconds = intervalMilliseconds
        this.isFastestInterval = isFastestInterval
        this.priorityLocation = priorityLocation
        this.notificationForForegroundService = notification
        this.dialogCallback = dialogCallback
    }

    class Builder(val applicationId: String,
                  val context: Context,
                  var notification: Notification) {
        var intervalMilliseconds: Long = LOCATION_INTERVAL_MILLISECONDS_DEFAULT
        var isFastestInterval: Boolean = false
        var priorityLocation: Int = PRIORITY_HIGH_ACCURACY
        var titleGpsConfirmDialog: String? = null
        var messageGpsConfirmDialog: String? = null
        var positiveButtonGpsConfirmDialog: String? = null
        var negativeButtonGpsConfirmDialog: String? = null
        var isCancelablePermission = false
        var titleAllowPermissionDialog: String? = null
        var messageAllowPermissionDialog: String? = null
        var positiveButtonAllowPermissionDialog: String? = null
        var negativeButtonAllowPermissionDialog: String? = null
        var isCancelableGps = false
        var dialogCallback: DialogCallback? = null
        fun build() = GpsProviderImpl(this)
    }

    override val applicationId: String

    override val weekContext: WeakReference<Context>

    override val notificationForForegroundService: Notification

    override val intervalMilliseconds: Long

    override val isFastestInterval: Boolean

    override val priorityLocation: Int

    override val titleGpsConfirmDialog: String?

    override val messageGpsConfirmDialog: String?

    override val positiveButtonGpsConfirmDialog: String?

    override val titleAllowPermissionDialog: String?

    override val messageAllowPermissionDialog: String?

    override val positiveButtonAllowPermissionDialog: String?

    override val isCancelableGps: Boolean

    override val isCancelablePermission: Boolean

    override val negativeButtonGpsConfirmDialog: String?

    override val negativeButtonAllowPermissionDialog: String?

    private var dialogCallback: DialogCallback?

    override fun showDialogGps(): Boolean {
        return dialogCallback?.showDialogGps() ?: false
    }

    override fun showDialogPermission(): Boolean {
        return dialogCallback?.showDialogPermission() ?: false
    }

    interface DialogCallback {
        fun showDialogGps(): Boolean
        fun showDialogPermission(): Boolean
    }
}