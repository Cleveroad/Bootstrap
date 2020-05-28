package com.cleveroad.hashtag_example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cleveroad.bootstrap.kotlin_hashtag_textwatcher.HashTagTextWatcher
import com.cleveroad.bootstrap.kotlin_hashtag_textwatcher.HashTagTextWatcherCallback
import kotlinx.android.synthetic.main.activity_hashtag.*

class HashTagActivity : AppCompatActivity(), HashTagTextWatcherCallback {

    companion object {
        fun start(context: Context) = context.run {
            startActivity(Intent(this, HashTagActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cleveroad.bootstrap.kotlin.R.layout.activity_hashtag)

        etInputText.apply {
            addTextChangedListener(HashTagTextWatcher(this@HashTagActivity,
                                                      this,
                                                      defaultCharacter = '$',
                                                      highlightTextColor = Color.MAGENTA))
        }

        bSetText.setOnClickListener {
            tvOutputText.apply {
                highlightColor = Color.TRANSPARENT
                text = etInputText.text
                movementMethod = LinkMovementMethod.getInstance()
                clearComposingText()
            }
        }
    }

    override fun onWordClicked(word: String) {
        (getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.apply {
            setPrimaryClip(ClipData.newPlainText("def_label", word))
        }
        Toast.makeText(this@HashTagActivity, word, Toast.LENGTH_SHORT).show()
    }
}
