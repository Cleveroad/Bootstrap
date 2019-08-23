package com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.cleveroad.bootstrap.kotlin_auth.R
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.AuthHandler.Companion.EXTRA_AUTH_ERROR
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.AuthHandler.Companion.EXTRA_TOKEN
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.ConfigProvider.config
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.data.TWITTER_AUTH_REQUEST_CODE
import com.cleveroad.bootstrap.kotlin_auth.twitter.client.oauth.PARAM_VERIFIER
import com.cleveroad.bootstrap.kotlin_core.ui.BASE_SNACK_BAR_DURATION
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleActivity
import com.cleveroad.bootstrap.kotlin_core.ui.NO_ID
import kotlinx.android.synthetic.main.activity_oauth.*

class OAuthActivity : BaseLifecycleActivity<OAuthVM>() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivityForResult(newIntent(activity), TWITTER_AUTH_REQUEST_CODE)
        }

        private fun newIntent(context: Context) =
                Intent(context, OAuthActivity::class.java)
    }

    override val viewModelClass = OAuthVM::class.java

    override val containerId = NO_ID

    override val layoutId = R.layout.activity_oauth

    override fun getProgressBarId() = NO_ID

    override fun getSnackBarDuration() = BASE_SNACK_BAR_DURATION

    private val authUrlObserver = Observer<String> { url ->
        openUrl(url)
    }

    private val accessTokenObserver = Observer<String> { accessToken ->
        deliverResult(accessToken)
    }

    override fun observeLiveData() = viewModel.run {
        authorizationUrlLD.observe(this@OAuthActivity, authUrlObserver)
        accessTokenLD.observe(this@OAuthActivity, accessTokenObserver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        viewModel.startOAuthFlow()
    }

    override fun processError(error: Any) {
        (error as? String)?.let {
            deliverResult(errorMessage = it)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupUi() {
        wvOauth?.run {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        config?.callbackUrl?.let { redirectUrl ->
                            request?.url?.takeIf { it.toString().contains(redirectUrl) }?.let { url ->
                                handleRedirect(url)
                            }
                        }
                    }
                    return true
                }

                @Suppress("DEPRECATION")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    config?.callbackUrl?.let { redirectUrl ->
                        url?.takeIf { it.contains(redirectUrl) }?.let {
                            handleRedirect(Uri.parse(url))
                        }
                    }
                    return true
                }
            }
        }
    }

    private fun openUrl(url: String) {
        wvOauth?.loadUrl(url)
    }

    private fun handleRedirect(uri: Uri) {
        uri.getQueryParameter(PARAM_VERIFIER)?.let { verifier ->
            viewModel.getAccessToken(verifier)
        }
    }

    private fun deliverResult(accessToken: String? = null,
                              errorMessage: String? = null) {
        Intent().let { resultIntent ->
            accessToken?.let {
                setResult(Activity.RESULT_OK, resultIntent.putExtra(EXTRA_TOKEN, it))
            }
            errorMessage?.let {
                setResult(Activity.RESULT_CANCELED, resultIntent.putExtra(EXTRA_AUTH_ERROR, it))
            }
            finish()
        }
    }
}