package com.cleveroad.bootstrap.kotlin_permissionrequest


import android.app.Activity
import androidx.core.app.ActivityCompat


class PermissionActivityRequest(private val activity: Activity,
                                private val requestCode: Int,
                                permissions: List<String>)
    : BasePermissionRequest(activity, requestCode, permissions) {

    /**
     * Gets whether you should show UI with rationale for requesting a permission
     * @param permission [String] the requested permission
     * @return [Boolean] whether you can show permission rationale UI.
     */
    override fun shouldShowRational(permission: String) = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

    /**
     * Requests permissions to be granted to this application
     */
    override fun request() = ActivityCompat.requestPermissions(activity, filterNotGrantedPermission(permissions), requestCode)
}