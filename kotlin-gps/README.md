# Kotlin GPS [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>

## Meet Kotlin GPS by Cleveroad

Used to track user location via GPS.

### Description ###
A library that allows tracking location of a user via GPS. Work with geolocation is implemented in
the foreground service, which also allows for the determination of the user's location in the background.

## Setup and usage
### Installation
by gradle : 
```groovy
dependencies {
    implementation 'com.cleveroad.bootstrap:kotlin-gps:2.0.0'
}
```

### Usage ###
```groovy

 class GpsActivity : AppCompatActivity(), GpsUiDelegate.GpsUiCallback {
 
     private val notification by lazy {
         NotificationUtils(this).createNotification()
     }
 
     private val gpsProvider by lazy {
         GpsProviderImpl.Builder(BuildConfig.APPLICATION_ID, this, notification).apply {
             dialogCallback = object : GpsProviderImpl.DialogCallback {
                 override fun showDialogGps(): Boolean {
                     Toast.makeText(this@GpsActivity, "Gps", Toast.LENGTH_LONG).show()
                     return false
                 }
 
                 override fun showDialogPermission(): Boolean {
                     Toast.makeText(this@GpsActivity, "permission", Toast.LENGTH_LONG).show()
                     return false
                 }
             }
         }.build()
     }
 
     private val gpsUiDelegate by lazy {
         GpsUiDelegateImpl(this, gpsProvider)
     }
 
     private val locationObserver = Observer<Location?> { location ->
         location?.run { tvText.text = "$latitude $longitude" }
     }
 
     private val locationIsTracking = Observer<Boolean?> { isTracking ->
         isTracking?.let { tvIsTracking.text = it.toString() }
     }
 
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_gps)
         LocationProvider.run {
             locationLiveData.observe(this@GpsActivity, locationObserver)
             isTrackingLD.observe(this@GpsActivity, locationIsTracking)
         }
     }
 
     override fun getActivity(): AppCompatActivity = this
 
     override fun onPause() {
         super.onPause()
         gpsUiDelegate.onPause()
     }
 
     override fun onResumeFragments() {
         super.onResumeFragments()
         gpsUiDelegate.onResumeFragments()
     }
 
     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         gpsUiDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
     }
 
     override fun onDestroy() {
         gpsUiDelegate.onDestroy()
         super.onDestroy()
     }
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
