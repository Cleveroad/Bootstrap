package com.cleveroad.auth_example

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin.BuildConfig
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_auth.base.AuthProxy
import com.cleveroad.bootstrap.kotlin_auth.base.AuthType
import com.cleveroad.bootstrap.kotlin_auth.base.AuthType.*
import com.cleveroad.bootstrap.kotlin_auth.facebook.FacebookAuthCallback
import com.cleveroad.bootstrap.kotlin_auth.facebook.FacebookPermission
import com.cleveroad.bootstrap.kotlin_auth.google.GoogleAuthCallback
import com.cleveroad.bootstrap.kotlin_auth.linkedin.LITE_PROFILE
import com.cleveroad.bootstrap.kotlin_auth.linkedin.LinkedInAuthCallback
import com.cleveroad.bootstrap.kotlin_auth.twitter.TwitterAuthCallback
import com.cleveroad.bootstrap.kotlin_core.ui.NO_TITLE
import com.cleveroad.bootstrap.kotlin_core.ui.NO_TOOLBAR
import com.cleveroad.bootstrap.kotlin_ext.setClickListeners
import com.cleveroad.phone_example.BaseFragment
import com.google.android.gms.common.Scopes
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : BaseFragment<AuthVM>(),
        View.OnClickListener,
        GoogleAuthCallback,
        FacebookAuthCallback,
        TwitterAuthCallback,
        LinkedInAuthCallback {

    companion object {
        fun newInstance() = AuthFragment()
    }

    override val viewModelClass = AuthVM::class.java

    override val layoutId = R.layout.fragment_auth

    private val authProxy = AuthProxy()

    override fun observeLiveData() = Unit

    override fun getScreenTitle() = NO_TITLE

    override fun hasToolbar() = false

    override fun getToolbarId() = NO_TOOLBAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authProxy.run {
            registerGoogleAuthHelper(BuildConfig.GOOGLE_CLIENT_ID,
                    requireContext(),
                    listOf(Scopes.PROFILE),
                    this@AuthFragment)

            registerFacebookAuthHelper(listOf(FacebookPermission.PUBLIC_PROFILE),
                    this@AuthFragment)

            registerTwitterAuthHelper(BuildConfig.TWITTER_CONSUMER_KEY,
                    BuildConfig.TWITTER_CONSUMER_SECRET,
                    BuildConfig.TWITTER_REDIRECT_URL,
                    this@AuthFragment)

            registerLinkedInAuthHelper(BuildConfig.LINKEDIN_CLIENT_ID,
                    BuildConfig.LINKEDIN_CLIENT_SECRET,
                    listOf(LITE_PROFILE),
                    BuildConfig.LINKEDIN_REDIRECT_URL,
                    this@AuthFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(bGoogle, bFacebook, bTwitter, bLinkedIn)
    }

    override fun onResume() {
        super.onResume()
        authProxy.connect()
    }

    override fun onPause() {
        authProxy.disconnect()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authProxy.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(v: View?) = authProxy.run {
        when (v?.id) {
            R.id.bGoogle -> auth(GOOGLE_PLUS_AUTH)
            R.id.bFacebook -> auth(FACEBOOK_AUTH)
            R.id.bTwitter -> auth(TWITTER_AUTH)
            R.id.bLinkedIn -> auth(LINKEDIN_AUTH)
            else -> Unit
        }
    }

    override fun onSuccess(authType: AuthType, token: String) {
        showSnackBar("Auth successful: $token")
    }

    override fun onFail(authType: AuthType, throwable: Throwable?) {
        showSnackBar("Auth fail: ${throwable?.message}")
    }

    override fun onCancel() {
        showSnackBar("Auth Cancelled")
    }

    override fun getActivityForResult() = activity
}