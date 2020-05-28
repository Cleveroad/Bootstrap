package com.cleveroad.bootstrap.kotlin_auth.twitter.client.sso

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.AuthCallback
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.AuthHandler
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.ConfigProvider.config
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.data.AuthConfig
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.data.TWITTER_AUTH_REQUEST_CODE
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.utils.SignatureUtils.checkAppSignature
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.utils.isActivityAvailable

class SSOAuthHandler(callback: AuthCallback) : AuthHandler(callback) {

    override fun authorize(activity: Activity): Boolean =
            startAuthActivityForResult(activity)

    private fun startAuthActivityForResult(activity: Activity): Boolean =
            availableSSOPackage(activity.packageManager)?.let { ssoPackage ->
                Intent().setComponent(ComponentName(ssoPackage, SSO_CLASS_NAME)).let { ssoIntent ->
                    if (!ssoIntent.isActivityAvailable(activity)) return false
                    ssoIntent.run {
                        putExtra(EXTRA_CONSUMER_KEY, config?.consumerKey)
                        putExtra(EXTRA_CONSUMER_SECRET, config?.consumerSecret)
                    }
                    activity.startActivityForResult(ssoIntent, TWITTER_AUTH_REQUEST_CODE)
                }
                return true
            } ?: false

    private fun availableSSOPackage(packageManager: PackageManager) =
            TWITTER_PACKAGE_NAME.takeIf {
                checkAppSignature(packageManager, TWITTER_PACKAGE_NAME, TWITTER_SIGNATURE)
            } ?: DOGFOOD_PACKAGE_NAME.takeIf {
                checkAppSignature(packageManager, DOGFOOD_PACKAGE_NAME, DOGFOOD_SIGNATURE)
            }
}