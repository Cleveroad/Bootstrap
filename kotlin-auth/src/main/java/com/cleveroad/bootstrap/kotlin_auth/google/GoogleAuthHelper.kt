package com.cleveroad.bootstrap.kotlin_auth.google

import android.content.Context
import android.content.Intent
import com.cleveroad.bootstrap.kotlin_auth.base.AuthHelper
import com.cleveroad.bootstrap.kotlin_auth.base.AuthType.*
import com.cleveroad.bootstrap.kotlin_ext.doAsync
import com.cleveroad.bootstrap.kotlin_ext.safeLet
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.lang.ref.WeakReference

class GoogleAuthHelper(private val serverClientId: String,
                       private val context: Context,
                       private val scopes: List<String>,
                       callback: GoogleAuthCallback) : AuthHelper,
        GoogleApiClient.OnConnectionFailedListener {

    companion object {
        private const val REQUEST_CODE_GOOGLE_PLUS = 1
        private const val SCOPE_PREFIX = "oauth2: "
        private const val SCOPE_SEPARATOR = " "
    }

    private var googleApiClient: GoogleApiClient? = null

    private var callbackRef: WeakReference<GoogleAuthCallback> = WeakReference(callback)

    private var disposable: Disposable? = null

    private val successConsumer = Consumer<String> { onSuccess(it) }

    private val errorConsumer = Consumer<Throwable> {
        if (it is GetTokenException) {
            (it.cause as? UserRecoverableAuthException)?.apply {
                callback.startActivityForResult(intent, REQUEST_CODE_GOOGLE_PLUS)
            }
        } else {
            callback.onFail(GOOGLE_PLUS_AUTH, it)
        }
    }

    override fun auth() {
        safeLet(googleApiClient?.takeIf { it.isConnected }, callbackRef.get()) { googleApiClient, callback ->
            callback.startActivityForResult(
                    Auth.GoogleSignInApi.getSignInIntent(googleApiClient),
                    REQUEST_CODE_GOOGLE_PLUS)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_GOOGLE_PLUS) {
            disposable?.dispose()
            disposable = Flowable.just(Auth.GoogleSignInApi.getSignInResultFromIntent(data))
                    .filter { it.isSuccess }
                    .map { it.signInAccount }
                    .map {
                        GoogleAuthUtil.getToken(context,
                                it.account,
                                SCOPE_PREFIX + retrieveScopes())
                    }
                    .switchIfEmpty(Flowable.error(Exception("Authentication error!")))
                    .doAsync(successConsumer, errorConsumer)
        }
    }

    override fun connect() {
        initHelper()
    }

    override fun disconnect() {
        googleApiClient?.disconnect()
        disposable?.dispose()
    }

    override fun onConnectionFailed(result: ConnectionResult) = Unit

    private fun initHelper() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(serverClientId)
                .build()

        googleApiClient = GoogleApiClient.Builder(context)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build().apply { connect() }
    }

    private fun onSuccess(token: String) {
        callbackRef.get()?.onSuccess(GOOGLE_PLUS_AUTH, token)
    }

    private fun retrieveScopes() = scopes.joinToString(SCOPE_SEPARATOR)

    private class GetTokenException : RuntimeException()
}