package com.cleveroad.bootstrap.kotlin_ext

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * @return the text the TextView is displaying
 */
fun TextView.getStringText() = this.text.toString()

/**
 * Return the trimmed text the TextView is displaying
 */
fun TextView.getTrimStringText() = this.text.trim().toString()

/**
 * Register a callback to be invoked when focus of this view changed
 */
fun TextView.addFocusChangedListener(listener: (isHasFocus: Boolean) -> Unit) =
        this.setOnFocusChangeListener { _, hasFocus ->
            listener(hasFocus)
        }

/**
 * Sets the typeface and style in which the text should be displayed
 *
 * @param id The desired resource identifier.
 */
fun TextView.setFont(id: Int) {
    typeface = ResourcesCompat.getFont(context, id)
}

/**
 * Sets the text color for all the states (normal, selected, focused) to be this color.
 *
 * @param id The desired resource identifier.
 */
fun TextView.setTextColorCompat(@ColorRes id: Int) {
    setTextColor(context.getColorCompat(id))
}

fun TextView.makeBoldTypefaceSpan(linkText: String) {
    val spannable = SpannableString(text)
    val startPosition = text.indexOf(linkText, 0, false)
    if (startPosition >= 0 && startPosition + linkText.length <= text.length) {
        spannable.setSpan(StyleSpan(Typeface.BOLD), startPosition,
                startPosition + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    setText(spannable, TextView.BufferType.SPANNABLE)
}

fun TextView.makeItalicTypefaceSpan(linkText: String) {
    text = (SpannableStringBuilder(linkText).apply { setSpan(StyleSpan(Typeface.ITALIC), 0, linkText.length, 0) })
}

fun TextView.selectLink(linkText: String, @ColorRes colorRes: Int, isUnderline: Boolean = true, urlSpan: ClickableSpan? = null) {
    val spannable = SpannableString(text)
    val startPosition = text.indexOf(linkText, 0, false)
    if (startPosition >= 0 && startPosition + linkText.length <= text.length) {

        urlSpan?.let {
            spannable.setSpan(it, startPosition, startPosition + linkText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, colorRes)), startPosition,
                startPosition + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        if (isUnderline) {
            spannable.setSpan(UnderlineSpan(), startPosition,
                    startPosition + linkText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    movementMethod = LinkMovementMethod.getInstance()
    setText(spannable, TextView.BufferType.SPANNABLE)
}
