# Kotlin Search User TextWatcher [![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome) <img src="https://www.cleveroad.com/public/comercial/label-android.svg" height="19"> <a href="https://www.cleveroad.com/?utm_source=github&utm_medium=label&utm_campaign=contacts"><img src="https://www.cleveroad.com/public/comercial/label-cleveroad.svg" height="19"></a>

## Meet Kotlin Search User TextWatcher by Cleveroad

Library that allows to enable search while typing a word preceded by a special character.


## Setup and usage
### Installation
by gradle :
```groovy
dependencies {
    implementation 'com.cleveroad.bootstrap:kotlin-search-user-textwatcher:3.0.0'
}
```

### Usage ###


```kotlin
class SearchUserActivity : AppCompatActivity(), SearchUserTextWatcherCallback {

    ...

    private val mockUsers =
        arrayOf("Virgina L. Albino", "Pedro K. Ellis", "Thomas J. Guardado", "Aaron C. Kirby")

    private val searchUserTextWatcher = SearchUserTextWatcher(
        this@SearchUserActivity,
        '/'
    )

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
        searchUsersAdapter = ArrayAdapter(
            this@SearchUserActivity,
            android.R.layout.simple_dropdown_item_1line,
            arrayListOf()
        )
        etInputText.apply {
            addTextChangedListener(searchUserTextWatcher)
            setTokenizer(CharacterTokenizer('/'))
            setAdapter(searchUsersAdapter)
        }
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