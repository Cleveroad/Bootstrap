package com.cleveroad

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cleveroad.auth_example.SampleAuthActivity
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_ext.setClickListenerWithDebounce
import com.cleveroad.composite_adapter.CompositeAdapterActivity
import com.cleveroad.compress_image.CompressImageActivity
import com.cleveroad.ffmpeg_example.FFMpegActivity
import com.cleveroad.gps_example.GpsActivity
import com.cleveroad.hashtag_example.HashTagActivity
import com.cleveroad.phone_example.PhoneActivity
import com.cleveroad.progress_upload_example.ui.ProgressUploadActivity
import com.cleveroad.search_user_example.SearchUserActivity
import com.cleveroad.validator_example.ValidatorActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClickListenerWithDebounce(bValidationExample,
                bGpsExample,
                bFfmpegExample,
                bCompressImage,
                bPhoneViewExample,
                bAuthExample,
                bHashtagExample,
                bSearchUser,
                bProgressUpload,
                bCompositeAdapter)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bValidationExample -> ValidatorActivity.start(this)
            R.id.bGpsExample -> GpsActivity.start(this)
            R.id.bFfmpegExample -> FFMpegActivity.start(this)
            R.id.bCompressImage -> CompressImageActivity.start(this)
            R.id.bPhoneViewExample -> PhoneActivity.start(this)
            R.id.bAuthExample -> SampleAuthActivity.start(this)
            R.id.bHashtagExample -> HashTagActivity.start(this)
            R.id.bSearchUser -> SearchUserActivity.start(this)
            R.id.bProgressUpload -> ProgressUploadActivity.start(this)
            R.id.bCompositeAdapter -> CompositeAdapterActivity.start(this)
        }
    }
}