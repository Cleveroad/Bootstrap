# Kotlin ffmpeg video compress [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>

## Meet Kotlin ffmpeg video compress by Cleveroad

This is a compression library which helps you to reduce video file size both in default and custom way.
It allows you to control you the approximate output file size, codecs, audio and video bitrate and compress rate of input video while maintaining it quality.

## Setup and usage
### Installation
by gradle : 
```groovy
dependencies {
    implementation 'com.cleveroad.bootstrap:kotlin-ffmpeg-video-compress:2.0.0'
}
```

### Usage ###

Before use make sure you  have added the following permission to your project
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

```groovy

 class FFMpegActivity : AppCompatActivity(), View.OnClickListener {

        override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         ...
         setClickListeners(bCreateThumbnails)
     }

     override fun onClick(view: View) {
         when (view.id) {
             R.id.bCreateThumbnails -> {
              requestWriteStoragePermission {
                                 setProgressVisibility(true)
                                 CompressVideoBuilder.with(this)
                                         .setInput(inputFilePath)
                                         .setOutputPath(outputFilePath)
                                         .setApproximateVideoSizeMb(10)
                                         .execute({ result -> onResultCompressVideo(result) },
                                                 { error -> onError(error) },
                                                 { progress -> onProgress(progress) })
                             }
             }
         }
     }

    private fun onResultCompressVideo(pathname: String) {
        val inputSize = File(tvPickedFile.text.toString()).length()
        val outputSize = File(pathname).length()
        setProgressVisibility(false)
        tvPickedOverlayFile.text = "Picked file size - $inputSize\n" +
                "Output file size - $outputSize\n" +
                "Reduce difference - ${inputSize / outputSize}"
    }

     private fun onError(error: Throwable) {
         error.message?.let { Log.e(TAG, it) }
     }

     private fun onProgress(progress: Long) {
         tvProgress.text = progress.toString()
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
