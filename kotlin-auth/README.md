# Kotlin Auth [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>

## Meet Kotlin Auth by Cleveroad

Library for implementing authorization in the application through social networks, such as Facebook, Google, LinkedIn, Twitter.

## Setup and usage
### Installation
by gradle : 
```groovy
dependencies {
    implementation 'com.cleveroad.bootstrap:kotlin-auth:3.0.0'
}
```

### Usage ###

Before use make sure you have added the following permission to your project

```xml
 <uses-permission android:name="android.permission.INTERNET"/>
```

```kotlin

 class AuthFragment : BaseLifecycleFragment<AuthVM>(),
         View.OnClickListener,
         GoogleAuthCallback,
         FacebookAuthCallback,
         TwitterAuthCallback,
        LinkedInAuthCallback,
        InstagramAuthCallback {

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

             registerInstagramAuthHelper(BuildConfig.INSTAGRAM_CLIENT_ID,
                     BuildConfig.INSTAGRAM_CLIENT_SECRET,
                     listOf(USER_PROFILE),
                     BuildConfig.INSTAGRAM_REDIRECT_URL,
                     this@AuthFragment)
         }
     }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         setClickListeners(bGoogle, bFacebook, bTwitter, bLinkedIn, bInstagram)
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
             R.id.bInstagram -> auth(INSTAGRAM_AUTH)
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

```

### Support ###
If you have any questions, issues or propositions, please create a <a href="../../issues/new">new issue</a> in this repository.

If you want to hire us, send an email to sales@cleveroad.com or fill the form on <a href="https://www.cleveroad.com/contact">contact page</a>

Follow us:

[![Awesome](/images/social/facebook.png)](https://www.facebook.com/cleveroadinc/)   [![Awesome](/images/social/twitter.png)](https://twitter.com/cleveroadinc)   [![Awesome](/images/social/google.png)](https://plus.google.com/+CleveroadInc)   [![Awesome](/images/social/linkedin.png)](https://www.linkedin.com/company/cleveroad-inc-)   [![Awesome](/images/social/youtube.png)](https://www.youtube.com/channel/UCFNHnq1sEtLiy0YCRHG2Vaw)
<br/>

### License ###
* * *
    The MIT License (MIT)
    
    Copyright (c) 2016 Cleveroad Inc.
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
