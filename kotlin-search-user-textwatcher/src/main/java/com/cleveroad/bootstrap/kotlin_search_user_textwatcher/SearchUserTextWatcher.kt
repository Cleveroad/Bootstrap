package com.cleveroad.bootstrap.kotlin_search_user_textwatcher

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import java.lang.ref.WeakReference

/**
 * Custom [TextWatcher] which allows you to find words that begin with [character].
 *
 * @param searchUserCallback [SearchUserTextWatcherCallback] callback to which the found text will
 * be passed.
 * @property character [Char] character which triggers search.
 */
class SearchUserTextWatcher(searchUserCallback: SearchUserTextWatcherCallback,
                            private val character: Char = AT_CHARACTER) : TextWatcher {

    companion object {
        private const val LOG_TAG = "SearchUserTextWatcher"
        private const val NO_ELEMENT_EXCEPTION = "No such element exception"
    }

    private val searchUserWR = WeakReference(searchUserCallback)

    /**
     * This method is called to notify you that, within [text], the [count] characters beginning at
     * [start] are about to be replaced by new [text] with length [after].
     *
     * @param text [CharSequence] current text inside [EditText].
     * @param start [Int] current cursor position.
     * @param count [Int] characters to be replaced.
     * @param after [Int] text length after the text changes.
     *
     * @return [Unit]
     */
    override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) = Unit

    /**
     * This method is called to notify you that, within [text], the [count] characters beginning at
     * [start] have just replaced old text that had length [before].
     *
     * @param text [CharSequence] current text inside [EditText].
     * @param start [Int] current cursor position.
     * @param count [Int] characters to be replaced.
     * @param before [Int] length of old text.
     *
     * @return [Unit]
     */
    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        checkCharacter(text, start)
    }

    /**
     * This method is called to notify you that, somewhere within [text], the text has been changed.
     *
     * @param text [CharSequence] current text inside [EditText].
     *
     * @return [Unit]
     */
    override fun afterTextChanged(text: Editable?) = Unit

    /**
     * Check if [text] at [start] position begins with [character].
     *
     * @param text [CharSequence] current text inside [EditText].
     * @param start [Int] current cursor position.
     *
     * @return [Unit]
     */
    private fun checkCharacter(text: CharSequence?, start: Int) {
        text?.takeIf { it.isNotBlank() }?.let {
            for (index in start - 1 downTo 0) {
                if (it[index] == character) {
                    getCurrentWord(it, index)
                    break
                }
            }
        }
    }

    /**
     * Get word from [text] which match [USER_PATTERN] and starts at [startIndex] position. Remove
     * [character] and pass result into [searchUserWR] [SearchUserTextWatcherCallback].
     *
     * @param text [CharSequence] current text inside [EditText].
     * @param startIndex [Int] start position of word which match [USER_PATTERN].
     *
     * @return [Unit]
     */
    private fun getCurrentWord(text: CharSequence, startIndex: Int) {
        try {
            Regex(pattern =  "\\$character\\w+").find(text, startIndex)?.value?.let { query ->
                searchUserWR.get()?.searchUser(query.removeRange(0, 1))
            }
        } catch (noSuchElementException: NoSuchElementException) {
            Log.i(LOG_TAG, NO_ELEMENT_EXCEPTION)
        }
    }
}