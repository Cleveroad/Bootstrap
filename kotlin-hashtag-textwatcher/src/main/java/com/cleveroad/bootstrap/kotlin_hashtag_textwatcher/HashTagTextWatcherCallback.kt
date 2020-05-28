package com.cleveroad.bootstrap.kotlin_hashtag_textwatcher

/**
 * Implement this interface in the class in which you want to process click on a highlighted word.
 */
interface HashTagTextWatcherCallback {

    /**
     * Process the click on a highlighted word.
     *
     * @param word [String] clicked word.
     */
    fun onWordClicked(word: String)

}