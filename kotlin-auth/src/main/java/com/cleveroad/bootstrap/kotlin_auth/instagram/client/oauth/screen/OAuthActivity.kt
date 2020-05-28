package com.cleveroad.bootstrap.kotlin_auth.instagram.client.oauth.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.cleveroad.bootstrap.kotlin_auth.R
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.AuthHandler.Companion.EXTRA_ACCESS_TOKEN
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.AuthHandler.Companion.EXTRA_AUTH_ERROR
import com.cleveroad.bootstrap.kotlin_auth.instagram.client.data.INSTAGRAM_AUTH_REQUEST_CODE
import com.cleveroad.bootstrap.kotlin_core.ui.BASE_SNACK_BAR_DURATION
import com.cleveroad.bootstrap.kotlin_core.ui.BaseLifecycleActivity
import com.cleveroad.bootstrap.kotlin_core.ui.NO_ID
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils.Companion.getExtra
import com.cleveroad.bootstrap.kotlin_ext.returnTrue
import com.cleveroad.bootstrap.kotlin_ext.safeLet
import kotlinx.android.synthetic.main.activity_oauth.*

class OAuthActivity : BaseLifecycleActivity<OAuthVM>() {

    companion object {

        private val EXTRA_CLIENT_ID = getExtra<String>("CLIENT_ID")
        private val EXTRA_SECRET = getExtra<String>("SECRET")
        private val EXTRA_SCOPES = getExtra<String>("SCOPES")
        private val EXTRA_CALLBACK_URL = getExtra<String>("CALLBACK_URL")

        fun start(activity: Activity,
                  clientId: String,
                  clientSecret: String,
                  scopes: String,
                  callbackUrl: String) =
                activity.startActivityForResult(newIntent(activity, clientId,
                        clientSecret, scopes, callbackUrl), INSTAGRAM_AUTH_REQUEST_CODE).returnTrue()

        private fun newIntent(context: Context,
                              clientId: String,
                              clientSecret: String,
                              scopes: String,
                              callbackUrl: String) =
                Intent(context, OAuthActivity::class.java)
                        .putExtra(EXTRA_CLIENT_ID, clientId)
                        .putExtra(EXTRA_SECRET, clientSecret)
                        .putExtra(EXTRA_SCOPES, scopes)
                        .putExtra(EXTRA_CALLBACK_URL, callbackUrl)
    }

    override val viewModelClass = OAuthVM::class.java

    override val layoutId = R.layout.activity_oauth

    override val containerId = NO_ID

    override fun getProgressBarId() = NO_ID

    override fun getSnackBarDuration() = BASE_SNACK_BAR_DURATION

    private val authUrlObserver = Observer<String> { authUrl ->
        openUrl(authUrl)
    }

    private val tokenObserver = Observer<String> {
        deliverResult(accessToken = it)
    }

    override fun observeLiveData() = viewModel.run {
        authUrlLD.observe(this@OAuthActivity, authUrlObserver)
        accessTokenLD.observe(this@OAuthActivity, tokenObserver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getExtras()
        setupUi()
    }

    override fun processError(error: Any) {
        (error as? String)?.let { errMessage ->
            deliverResult(errorMessage = errMessage)
        }
    }

    private fun getExtras() {
        intent?.run {
            safeLet(getStringExtra(EXTRA_CLIENT_ID),
                    getStringExtra(EXTRA_SECRET),
                    getStringExtra(EXTRA_SCOPES),
                    getStringExtra(EXTRA_CALLBACK_URL)) { clientId, secret, scopes, callbackUrl ->
                viewModel.getAuthorizationUrl(clientId, secret, scopes, callbackUrl)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupUi() {
        wvOauth?.run {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?) =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            request?.url?.toString()?.let { viewModel.handleRedirect(it) } ?: false
                        } else false

                @Suppress("DEPRECATION")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean =
                        url?.let { viewModel.handleRedirect(it) } ?: false
            }
        }
    }

    private fun openUrl(url: String) {
        wvOauth?.loadUrl(url)
    }

    private fun deliverResult(accessToken: String? = null,
                              errorMessage: String? = null) {
        Intent().let { resultIntent ->
            accessToken?.let {
                setResult(Activity.RESULT_OK, resultIntent.putExtra(EXTRA_ACCESS_TOKEN, it))
            }
            errorMessage?.let {
                setResult(Activity.RESULT_CANCELED, resultIntent.putExtra(EXTRA_AUTH_ERROR, it))
            }
            finish()
        }
    }
}