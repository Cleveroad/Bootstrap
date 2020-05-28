package com.cleveroad.search_user_example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.cleveroad.bootstrap.kotlin.R
import com.cleveroad.bootstrap.kotlin_search_user_textwatcher.CharacterTokenizer
import com.cleveroad.bootstrap.kotlin_search_user_textwatcher.SearchUserTextWatcher
import com.cleveroad.bootstrap.kotlin_search_user_textwatcher.SearchUserTextWatcherCallback
import kotlinx.android.synthetic.main.activity_search_user.*

class SearchUserActivity : AppCompatActivity(), SearchUserTextWatcherCallback {

    companion object {
        fun start(context: Context) = context.run {
            startActivity(Intent(this, SearchUserActivity::class.java))
        }
    }

    private val mockUsers = arrayOf("Virgina L. Albino", "Pedro K. Ellis", "Thomas J. Guardado", "Aaron C. Kirby")

    private val searchUserTextWatcher = SearchUserTextWatcher(this@SearchUserActivity,
                                                              '/')

    private lateinit var searchUsersAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)
        enableSearchUserWatcher()
    }

    override fun searchUser(searchInput: String) {
        mockUsers.filter { it.toLowerCase().contains(searchInput.toLowerCase()) }
            .let {
                searchUsersAdapter.apply {
                    clear()
                    addAll(it)
                    notifyDataSetChanged()
                }
            }
    }

    private fun enableSearchUserWatcher() {
        searchUsersAdapter = ArrayAdapter(this@SearchUserActivity, android.R.layout.simple_dropdown_item_1line, arrayListOf())
        etInputText.apply {
            addTextChangedListener(searchUserTextWatcher)
            setTokenizer(CharacterTokenizer('/'))
            setAdapter(searchUsersAdapter)
        }
    }
}