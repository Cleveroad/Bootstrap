package com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.screen

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.PARAM_AUTH_CODE
import com.cleveroad.bootstrap.kotlin_auth.linkedin.client.oauth.network.NetworkModule
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel
import com.cleveroad.bootstrap.kotlin_ext.returnTrue
import com.cleveroad.bootstrap.kotlin_ext.safeLet

class OAuthVM(application: Application) : BaseLifecycleViewModel(application) {

    val authUrlLD = MutableLiveData<String>()
    val accessTokenLD = MutableLiveData<String>()

    private var clientId: String? = null
    private var secret: String? = null
    private var scopes: String? = null
    private var redirectUrl: String? = null

    private val authModule = NetworkModule.client.linkedInOAuth

    fun getAuthorizationUrl(clientId: String,
                            clientSecret: String,
                            scopes: String,
                            redirectUrl: String) {
        this.clientId = clientId
        this.secret = clientSecret
        this.scopes = scopes
        this.redirectUrl = redirectUrl

        authModule.getAuthorizationUrl(clientId, scopes, redirectUrl)
                .doAsync(authUrlLD)
    }

    fun handleRedirect(url: String): Boolean =
        redirectUrl?.let { redirectUrl ->
            url.takeIf { it.contains(redirectUrl) }?.let {
                Uri.parse(it).getQueryParameter(PARAM_AUTH_CODE)?.let { authCode ->
                    getAccessToken(authCode).returnTrue()
                }
            } ?: false
        } ?: false

    private fun getAccessToken(authorizationCode: String) {
        safeLet(clientId, secret, redirectUrl) { clientId, secret, redirectUrl ->
            authModule.getAccessToken(authorizationCode, redirectUrl, clientId, secret)
                    .doAsync(accessTokenLD)
        }
    }
}