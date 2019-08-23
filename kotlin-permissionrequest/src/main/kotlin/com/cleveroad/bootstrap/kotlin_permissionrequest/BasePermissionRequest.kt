package com.cleveroad.bootstrap.kotlin_permissionrequest

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


abstract class BasePermissionRequest(private val context: Context?,
                                     private val requestCode: Int,
                                     protected val permissions: List<String>)
    : PermissionProviderResult {
    private var permissionResult: PermissionResult? = null
    private var showRational = false

    init {
        checkIfPermissionPresentInAndroidManifest()
    }

    protected abstract fun shouldShowRational(permission: String): Boolean

    protected abstract fun request()

    /**
     * Check that user added following Storage permission in our manifest File
     */
    private fun checkIfPermissionPresentInAndroidManifest() = permissions.filter { !hasPermission(it) }.let {
        if (it.isNotEmpty()) {
            throw RuntimeException("${it.joinToString()} is not declared in manifest")
        }
    }

    /**
     * Check that user added following Storage permission in our manifest File
     * @param permission [String] the requested permission
     * @return [Boolean] true if has permission and false in another case
     */
    private fun hasPermission(permission: String) = context?.let {
        it.packageManager?.getPackageInfo(it.packageName, PackageManager.GET_PERMISSIONS)
                ?.requestedPermissions?.any { it == permission }
    } ?: false

    /**
     * Check if you have the permission, or not.
     * @param permissions [List]  the requested permissions
     * @return [Boolean] whether you can show permission rationale UI.
     */
    private fun checkSelfPermission(permissions: List<String>) = context?.let {
        permissions.any { permission -> ContextCompat.checkSelfPermission(it, permission) != PackageManager.PERMISSION_GRANTED }
    } ?: false

    /**
     * Requests permissions to be granted to this application
     * @param permissionResult [PermissionResult] result callback
     */
    override fun request(permissionResult: PermissionResult) {
        this.permissionResult = permissionResult
        if (checkSelfPermission(permissions)) {
            showRational = shouldShowRational(permissions)
            request()
        } else {
            permissionResult.onPermissionGranted()
        }
    }

    /**
     *  Gets whether you should show UI with rationale for requesting a permission
     * @param permissions [List] the requested permissions
     * @return [Boolean] whether you can show permission rationale UI.
     */
    private fun shouldShowRational(permissions: List<String>) = permissions.any { shouldShowRational(it) }

    /**
     *  Handle callback for the result from requesting permissions
     *  @param requestCode [Int] application specific request code to match with a result
     *  @param permissions [Array]  the requested permissions
     *  @param grantResults [IntArray] the grant results for the corresponding permissions
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (this.requestCode == requestCode) {
            var denied = false
            val grantedPermissions = mutableListOf<String>()
            grantResults.forEachIndexed { i, grantResult ->
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    denied = true
                } else {
                    grantedPermissions.add(permissions[i])
                }
            }
            permissionResult?.let {
                if (denied) {
                    val currentShowRational = shouldShowRational(permissions.asList())
                    if (!showRational && !currentShowRational) {
                        it.onPermissionDeniedBySystem()
                    } else {
                        if (grantedPermissions.isNotEmpty()) {
                            it.onSinglePermissionGranted(grantedPermissions)
                        }
                        it.onPermissionDenied()
                    }
                } else {
                    it.onPermissionGranted()
                }
            }
        }
    }

    /**
     * returns an array of not granted permissions
     *  @param permissions [List]  the requested permissions
     *  @return [Array]  the not granted permissions
     */
    protected fun filterNotGrantedPermission(permissions: List<String>) = context?.let { context ->
        permissions.filter { ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }.toTypedArray()
    } ?: arrayOf()
}