package com.cleveroad.bootstrap.kotlin_auth.twitter.client.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AuthConfig(var consumerKey: String?,
                      var consumerSecret: String?,
                      var callbackUrl: String?,
                      var requestToken: String? = null,
                      var requestTokenSecret: String? = null,
                      var verifier: String? = null): Parcelable