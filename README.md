# Kotlin Bootstrap [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>

## Meet Kotlin Bootstrap by Cleveroad

This is tutorial about android bootstrap library. This set of libraries is designed to help developers accomplish many various tasks easier and faster.

### Kotlin Auth
The library allowing to implement authorization via social networks in apps. Facebook, Google, LinkedIn, and Twitter are supported.

### Kotlin Core
The library contains implementations of basic classes of an Android application to build the app’s architecture faster. Kotlin Core has implementations for Activity, Fragment, AndroidViewModel, DialogFragment, ArrayAdapter. This library has the utils directory storing a lot of helper classes to work with images, file system, internet, etc.

### Kotlin Extensions
A set of extension functions making life easier for Android developers. 

### Kotlin GPS
This library is designed to help you implement user geoposition tracking via GPS. Work with geolocation is implemented in the foreground service, which also allows to track the user's location in the background.

### Kotlin Permissionrequest
Android application has to request permission to access confidential user data. This library enables you to quickly implement the logic necessary for requesting access to user data and certain system features as well.

### Kotlin PhoneInput
This library is an implementation of Custom ViewGroup which makes it easy and fast to implement fields for entering a phone number. The library can determine the country code. Phone numbers are formatted automatically depending on a chosen country. 

### Kotlin RxBus
RxBus is an excellent solution allowing objects with various lifecycles and placed on different hierarchy levels to communicate with each other. It’s based on RxJava 2.

### Kotlin Validators
This library is designed for validation of phone number fields, passwords, emails, dates, and names. There is a configuration of templates for validation, error descriptions and more.

### Kotlin ffmpeg thumbnails
The wrapper library over ffmpeg that’s intended for splitting video into pictures (preview).

### Kotlin ffmpeg video compress
The wrapper library over ffmpeg that’s designed to compress the size of a video file.

## Setup and usage
### Installation
by project gradle :
```groovy
ext {

    bootstrapVersion = "2.0.0"

    bootstrapDependencies = [
            auth          : "com.cleveroad.bootstrap:kotlin-auth:$bootstrapVersion",
            core          : "com.cleveroad.bootstrap:kotlin-core:$bootstrapVersion",
            ext           : "com.cleveroad.bootstrap:kotlin-ext:$bootstrapVersion"
            thumbnails    : "com.cleveroad.bootstrap:kotlin-ffmpeg-thumbnails:$bootstrapVersion"
            video_compress: "com.cleveroad.bootstrap:kotlin-ffmpeg-video-compress:$bootstrapVersion"
            gps           : "com.cleveroad.bootstrap:kotlin-gps:$bootstrapVersion"
            permission    : "com.cleveroad.bootstrap:kotlin-permissionrequest:$bootstrapVersion"
            phone_input   : "com.cleveroad.bootstrap:kotlin-phone-input:$bootstrapVersion"
            validators    : "com.cleveroad.bootstrap:kotlin-validators:$bootstrapVersion",
            rxBus         : "com.cleveroad.bootstrap:kotlin-rx-bus:$bootstrapVersion",
    ]
}
```

by app gradle :
```groovy

dependencies {

    // Bootstrap
    implementation bootstrapDependencies.auth
    implementation bootstrapDependencies.core
    implementation bootstrapDependencies.ext
    implementation bootstrapDependencies.thumbnails
    implementation bootstrapDependencies.video_compress
    implementation bootstrapDependencies.gps
    implementation bootstrapDependencies.permission
    implementation bootstrapDependencies.phone_input
    implementation bootstrapDependencies.validators
    implementation bootstrapDependencies.rxBus
}
```

### Usage ###

- [Kotlin Auth]
- [Kotlin Core]
- [Kotlin Extension]
- [Kotlin FFMpeg Thumbnails]
- [Kotlin FFMpeg Video Compress]
- [Kotlin GPS]
- [Kotlin Permission Request]
- [Kotlin Phone Input]
- [Kotlin RxBus]
- [Kotlin Validators]

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

[Kotlin Auth]: /kotlin-auth
[Kotlin Core]: /kotlin-core
[Kotlin Extension]: /kotlin-ext
[Kotlin FFMpeg Thumbnails]: /kotlin-ffmpeg-thumbnails
[Kotlin FFMpeg Video Compress]: /kotlin-ffmpeg-video-compress
[Kotlin GPS]: /kotlin-gps
[Kotlin Permission Request]: /kotlin-permissionrequest
[Kotlin Phone Input]: /kotlin-phone-input
[Kotlin RxBus]: /kotlin-rx-bus
[Kotlin Validators]: /kotlin-validators
