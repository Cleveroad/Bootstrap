package com.cleveroad.bootstrap.kotlin_permissionrequest

import androidx.fragment.app.Fragment


class PermissionFragmentRequest(private val fragment: Fragment,
                                private val requestCode: Int,
                                permissions: List<String>)
    : BasePermissionRequest(fragment.activity, requestCode, permissions) {

    /**
     * Gets whether you should show UI with rationale for requesting a permission
     * @param permission [String] the requested permission
     * @return [Boolean] whether you can show permission rationale UI.
     */
    override fun shouldShowRational(permission: String) = fragment.shouldShowRequestPermissionRationale(permission)

    /**
     * Requests permissions to be granted to this application
     */
    override fun request() = fragment.requestPermissions(filterNotGrantedPermission(permissions), requestCode)

}