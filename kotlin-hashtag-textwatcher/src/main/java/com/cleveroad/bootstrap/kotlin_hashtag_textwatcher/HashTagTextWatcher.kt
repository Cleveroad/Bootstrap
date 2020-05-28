package com.cleveroad.bootstrap.kotlin_hashtag_textwatcher

import android.graphics.Color
import android.text.*
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.text.getSpans
import java.lang.ref.WeakReference

/**
 * Custom [TextWatcher] which allows you to find words that begin with [defaultCharacter].
 * Add [LinkMovementMethod.getInstance()] movement method to [EditText] or [TextView] if you want
 * to activate word click processing.
 *
 * @param itemCallback [HashTagTextWatcherCallback] callback to which the clicked word will
 * be passed.
 * @param editText current [EditText].
 * @param defaultCharacter [Char] character which triggers word highlight.
 * @property defaultTextColor [Int] current text color.
 * @property highlightTextColor [Int] highlight text color.
 */
class HashTagTextWatcher(itemCallback: HashTagTextWatcherCallback,
                         editText: EditText,
                         defaultCharacter: Char = HASHTAG_CHARACTER,
                         @ColorInt private val defaultTextColor: Int = Color.BLACK,
                         @ColorInt private val highlightTextColor: Int = Color.BLUE) : TextWatcher {

    private val itemWR = WeakReference(itemCallback)

    private val editTextWR = WeakReference(editText)

    private val highlightCharacter = defaultCharacter

    init {
        editTextWR.get()?.apply {
            setText(text, TextView.BufferType.SPANNABLE)
        }
    }

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
        text?.let { highlightWords(it) }
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
     * Find all words in [text] which start with [highlightCharacter], highlight them and make them
     * clickable.
     *
     * @param text [CharSequence] current text inside [EditText].
     *
     * @return [Unit]
     */
    private fun highlightWords(text: CharSequence) {
        val editTextSpannable = editTextWR.get()?.text as? Spannable
        clearSpans(editTextSpannable, text.length)
        Regex(pattern = "\\$highlightCharacter\\w+").findAll(editTextSpannable.toString()).forEach {
            with(it.range) {
                setClickableSpan(editTextSpannable, text.substring(first, last + 1), first, last + 1)
            }
        }
    }

    /**
     * Attach the foreground color span [ForegroundColorSpan] to the range [start]…[end] of the
     * [spannable].
     *
     * @param spannable [Spannable] current text.
     * @param color [Int] span color.
     * @param start [Int] span start.
     * @param end [Int] span end.
     * @param flag [Int] span flag [Spanned].
     *
     * @return [Unit]
     */
    private fun setForegroundColorSpan(spannable: Spannable?,
                                       @ColorInt color: Int,
                                       start: Int,
                                       end: Int,
                                       flag: Int = 0) {
        spannable?.setSpan(ForegroundColorSpan(color), start, end, flag)
    }

    /**
     * Attach the clickable span [ClickableSpan] to the range [start]…[end] of the [spannable].
     *
     * @param spannable [Spannable] current text.
     * @param textPart [String] clickable text part.
     * @param start [Int] span start.
     * @param end [Int] span end.
     *
     * @return [Unit]
     */
    private fun setClickableSpan(spannable: Spannable?, textPart: String, start: Int, end: Int) {
        spannable?.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                itemWR.get()?.onWordClicked(textPart.removeRange(0, 1))
            }

            override fun updateDrawState(drawState: TextPaint) {
                drawState.color = highlightTextColor
            }
        }, start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    /**
     * Clear all current spans from [spannable].
     *
     * @param spannable [Spannable] current text.
     * @param textLength [Int] current text length.
     *
     * @return [Unit]
     */
    private fun clearSpans(spannable: Spannable?, textLength: Int) {
        setForegroundColorSpan(spannable, defaultTextColor, 0, textLength)
        spannable?.getSpans<ClickableSpan>(0, textLength)
            ?.map { spannable.removeSpan(it) }
    }
}