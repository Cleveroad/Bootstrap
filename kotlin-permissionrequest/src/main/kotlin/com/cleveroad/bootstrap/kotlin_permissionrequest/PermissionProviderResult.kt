package com.cleveroad.bootstrap.kotlin_permissionrequest

interface PermissionProviderResult {
    /**
     *  Handle callback for the result from requesting permissions
     *  @param requestCode [Int] application specific request code to match with a result
     *  @param permissions [Array] the requested permissions
     *  @param grantResults [IntArray] the grant results for the corresponding permissions
     */
    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>,
                                   grantResults: IntArray)

    /**
     * Requests permissions to be granted to this application
     * @param permissionResult [PermissionResult]  result callback
     */
    fun request(permissionResult: PermissionResult)
}