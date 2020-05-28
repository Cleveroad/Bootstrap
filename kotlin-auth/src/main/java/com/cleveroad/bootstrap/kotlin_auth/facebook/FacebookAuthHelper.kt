package com.cleveroad.bootstrap.kotlin_auth.facebook

import android.app.Activity
import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper
import com.cleveroad.bootstrap.kotlin_auth.base.AuthType.FACEBOOK_AUTH
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.lang.ref.WeakReference

/**
 * Implementation of [AuthHelper] for authorization with Facebook
 */
class FacebookAuthHelper(callback: FacebookAuthCallback,
                         private var permissions: List<FacebookPermission>) : AuthHelper {

    private val callbackRef = WeakReference(callback)

    private val callbackManager = CallbackManager.Factory.create()

    private val loginManager = LoginManager.getInstance()

    private val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            onSuccess(loginResult.accessToken.token)
        }

        override fun onCancel() {
            this@FacebookAuthHelper.onCancel()
        }

        override fun onError(error: FacebookException) {
            onFail(error)
        }
    }

    init {
        loginManager.registerCallback(callbackManager, facebookCallback)
    }

    override fun connect() = Unit

    override fun disconnect() = Unit

    override fun auth() {
        callbackRef.get()?.getActivityForResult()?.let { activity ->
            login(activity, permissions.map { it() })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun login(activity: Activity, permissions: List<String>) {
        loginManager.run {
            logOut()
            logInWithReadPermissions(activity, permissions)
        }
    }

    private fun onSuccess(token: String) {
        callbackRef.get()?.onSuccess(FACEBOOK_AUTH, token)
    }

    private fun onFail(error: FacebookException) {
        callbackRef.get()?.onFail(FACEBOOK_AUTH, error)
    }

    private fun onCancel() {
        callbackRef.get()?.onCancel()
    }
}