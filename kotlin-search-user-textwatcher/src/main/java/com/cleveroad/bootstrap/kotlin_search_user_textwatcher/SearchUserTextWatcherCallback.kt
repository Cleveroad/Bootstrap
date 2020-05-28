package com.cleveroad.bootstrap.kotlin_search_user_textwatcher

/**
 * Implement this interface in the class in which you want to process the result of
 * [SearchUserTextWatcher] search.
 */
interface SearchUserTextWatcherCallback {

    /**
     * Process the result of [SearchUserTextWatcher] search.
     *
     * @param searchInput [String] text which [SearchUserTextWatcher] was found.
     */
    fun searchUser(searchInput: String)

}