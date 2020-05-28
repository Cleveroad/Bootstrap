package com.cleveroad.bootstrap.kotlin_permissionrequest

/**
 * interface for handle the permissions request response
 */
interface PermissionResult {

    fun onPermissionGranted()

    fun onSinglePermissionGranted(grantedPermission: List<String>) = Unit

    fun onPermissionDenied() = Unit

    fun onPermissionDeniedBySystem() = Unit
}