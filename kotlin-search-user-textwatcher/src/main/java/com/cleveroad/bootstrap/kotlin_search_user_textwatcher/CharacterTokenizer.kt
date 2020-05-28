package com.cleveroad.bootstrap.kotlin_search_user_textwatcher

import android.widget.MultiAutoCompleteTextView
import com.cleveroad.bootstrap.kotlin_search_user_textwatcher.extensions.isSpecialCharacter

/**
 * Custom tokenizer which allows you to find words that begin with "[character]". Pass this class
 * instance into [setTokenizer] method of the target [EditText].
 */
class CharacterTokenizer(private val character: Char = AT_CHARACTER) : MultiAutoCompleteTextView.Tokenizer {

    companion object {
        private const val EMPTY_STRING = ""
    }

    /**
     * Returns the start of the token that ends at offset [cursorPosition] within [text].
     *
     * @param text [CharSequence] current text inside [EditText]
     * @param cursorPosition [Int] current cursor position
     *
     * @return position of [character] with type [Int] or current cursor position [Int] if
     * [character] not found.
     */
    override fun findTokenStart(text: CharSequence?, cursorPosition: Int) =
        text?.let {
            var tokenStart: Int? = null

            for (index in cursorPosition - 1 downTo 0) {
                if (it[index] == character && index < cursorPosition) {
                    tokenStart = index.plus(1)
                    break
                }
            }

            tokenStart ?: cursorPosition
        } ?: cursorPosition

    /**
     * Returns the end of the token (minus trailing punctuation) that begins at offset [cursorPosition]
     * within [text].
     *
     * @param text [CharSequence] current text inside [EditText]
     * @param cursorPosition [Int] current cursor position
     *
     * @return position of special character [isSpecialCharacter] with type [Int] or current cursor
     * position [Int] if special character [isSpecialCharacter] not found.
     */
    override fun findTokenEnd(text: CharSequence?, cursorPosition: Int) =
        text?.let {
            var tokenEnd: Int? = null

            for (index in cursorPosition - 1..it.length) {
                if (it[index].isSpecialCharacter() || index == it.length) {
                    tokenEnd = index
                    break
                }
            }

            tokenEnd ?: cursorPosition
        } ?: cursorPosition

    /**
     * Returns text, modified, if necessary, to ensure that it ends with a token terminator (for
     * example a space or comma).
     *
     * @param text [CharSequence] current text inside [EditText]
     *
     * @return current text inside [EditText] with type [CharSequence] or [EMPTY_STRING] if it null.
     */
    override fun terminateToken(text: CharSequence?) = text ?: EMPTY_STRING
}