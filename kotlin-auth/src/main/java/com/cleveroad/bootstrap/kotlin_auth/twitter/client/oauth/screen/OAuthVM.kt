package com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.screen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.ConfigProvider.config
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.NetworkModule
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.network.modules.TwitterOAuthModule
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleViewModel
import io.reactivex.rxjava3.core.Single

class OAuthVM(application: Application) : BaseLifecycleViewModel(application) {

    val authorizationUrlLD = MutableLiveData<String>()
    val accessTokenLD = MutableLiveData<String>()

    private var oauthModule: TwitterOAuthModule = NetworkModule.client.twitterOAuth

    fun startOAuthFlow() {
        oauthModule.run {
            getRequestToken()
                    .doOnSuccess { response ->
                        response.run {
                            config?.requestToken = requestToken
                            config?.requestTokenSecret = requestTokenSecret
                        }
                    }
                    .flatMap { response ->
                        getAuthorizationUrl(response.requestToken)
                    }
                    .doAsync(authorizationUrlLD)
        }
    }

    fun getAccessToken(verifier: String) {
        Single.just(verifier)
                .doOnSuccess { config?.verifier = verifier }
                .flatMap { oauthModule.getAccessToken() }
                .doAsync(accessTokenLD)
    }
}