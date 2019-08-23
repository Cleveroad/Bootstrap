package com.cleveroad.bootstrap.kotlin_permissionrequest

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * class for permission requests
 */
open class PermissionRequest : PermissionResult {
    val permissionsRequests = mutableListOf<Pair<PermissionProviderResult, PermissionResult>>()
    var permissionProviderResult: PermissionProviderResult? = null

    companion object {
        private const val FIRST_POSITION = 0
        private const val ONE_ITEM = 1
    }

    /**
     * Requests permissions to be granted to this application.
     * If permissionsRequests list is not empty wait when last request will be done
     * @param activity [Activity] the target activity
     * @param requestCode [Int] application specific request code to match with a result
     * @param permission [Array] the requested permissions
     * @param permissionResult [PermissionResult] interface for handle the permissions request response
     */
    fun request(activity: Activity,
                requestCode: Int,
                permission: Array<String>,
                permissionResult: PermissionResult) {
        PermissionActivityRequest(activity, requestCode, permission.toList()).let {
            permissionsRequests.add(Pair(it, permissionResult))
            if (permissionsRequests.size == ONE_ITEM) it.request(this)
        }
    }

    /**
     * Requests permissions to be granted to this application.
     * If permissionsRequests list is not empty wait when last request will be done
     * @param fragment [Fragment] the target fragment
     * @param requestCode [Int] application specific request code to match with a result
     * @param permission [Array] the requested permissions
     * @param permissionResult [PermissionResult] interface for handle the permissions request response
     */
    fun request(fragment: Fragment,
                requestCode: Int,
                permission: Array<String>,
                permissionResult: PermissionResult) {
        PermissionFragmentRequest(fragment, requestCode, permission.toList()).let {
            permissionsRequests.add(Pair(it, permissionResult))
            if (permissionsRequests.size == ONE_ITEM) it.request(this)
        }
    }

    /**
     * handle the permissions request response
     */
    override fun onPermissionGranted() {
        if (permissionsRequests.isNotEmpty()) {
            permissionsRequests[FIRST_POSITION].second.onPermissionGranted()
            removeFirstAndCallNext()
        }
    }

    /**
     * handle the permissions request response
     */
    override fun onPermissionDenied() {
        if (permissionsRequests.isNotEmpty()) {
            permissionsRequests[FIRST_POSITION].second.onPermissionDenied()
            removeFirstAndCallNext()
        }
    }

    /**
     * handle the permissions request response
     */
    override fun onPermissionDeniedBySystem() {
        if (permissionsRequests.isNotEmpty()) {
            permissionsRequests[FIRST_POSITION].second.onPermissionDeniedBySystem()
            removeFirstAndCallNext()
        }
    }

    /**
     * handle the permissions request response
     */
    override fun onSinglePermissionGranted(grantedPermission: List<String>) {
        if (permissionsRequests.isNotEmpty()) {
            permissionsRequests[FIRST_POSITION].second.onSinglePermissionGranted(grantedPermission)
            removeFirstAndCallNext()
        }
    }

    /**
     * remove first item from permissionsRequests and request next permission from list
     */
    private fun removeFirstAndCallNext() {
        with(permissionsRequests) {
            removeAt(FIRST_POSITION)
            firstOrNull()?.let { it.first.request(it.second) }
        }
    }

    /**
     *  Handle callback for the result from requesting permissions
     *  @param requestCode [Int]  application specific request code to match with a result
     *  @param permissions [Array] the requested permissions
     *  @param grantResults [IntArray] the grant results for the corresponding permissions
     */
    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray) {
        if (permissionsRequests.isNotEmpty()) {
            permissionProviderResult = permissionsRequests[FIRST_POSITION].first
            permissionProviderResult?.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}