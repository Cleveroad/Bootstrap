package com.cleveroad.bootstrap.kotlin_auth.twitter.client.utils

import android.annotation.TargetApi
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

object SignatureUtils {

    fun checkAppSignature(packageManager: PackageManager,
                                  packageName: String,
                                  requiredSignature: String) =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                checkAppSignaturePrePie(packageManager, packageName, requiredSignature)
            } else {
                checkAppSignaturePie(packageManager, packageName, requiredSignature)
            }

    @Suppress("DEPRECATION")
    private fun checkAppSignaturePrePie(packageManager: PackageManager,
                                        packageName: String,
                                        requiredSignature: String): Boolean {
        val packageInfo: PackageInfo

        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (exc: PackageManager.NameNotFoundException) {
            return false
        }
        packageInfo.signatures?.forEach { signature ->
            if (requiredSignature != signature.toCharsString()) {
                return false
            }
        }
        return true
    }

    @TargetApi(Build.VERSION_CODES.P)
    private fun checkAppSignaturePie(packageManager: PackageManager,
                                     packageName: String,
                                     requiredSignature: String): Boolean {
        val packageInfo: PackageInfo

        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
        } catch (exc: PackageManager.NameNotFoundException) {
            return false
        }
        packageInfo.signingInfo.signingCertificateHistory?.forEach { signature ->
            if (requiredSignature != signature.toCharsString()) {
                return false
            }
        }
        return true
    }
}