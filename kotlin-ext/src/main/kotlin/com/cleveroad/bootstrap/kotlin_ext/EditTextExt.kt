package com.cleveroad.bootstrap.kotlin_ext

import android.view.MotionEvent
import android.widget.EditText

fun EditText.getStringText() = this.text.toString()

fun EditText.getTrimStringText() = this.text.trim().toString()

fun EditText.showKeyboardWhenReadyForced() {
    withNotNull(this) {
        afterMeasured {
            requestFocus()
            context?.showKeyboard()
        }
    }
}

fun EditText.disableEditable() {
    keyListener = null
    isFocusableInTouchMode = false
}

fun EditText.setTextWithSelection(text: String, selection: Int = text.length) {
    setText(text)
    setSelection(selection)
}

fun EditText.setOnDrawableClick(type: DrawablePositionTypes, callback: () -> Unit) {
    setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {

            when (type) {
                DrawablePositionTypes.END -> {
                    if (event.rawX >= (right - compoundDrawables[DrawablePositionTypes.END.type].bounds.width())) {
                        callback.invoke()
                        return@setOnTouchListener true
                    }
                }
                DrawablePositionTypes.START -> {
                    if (event.rawX <= (compoundDrawables[DrawablePositionTypes.START.type].bounds.width())) {
                        callback.invoke()
                        return@setOnTouchListener true
                    }
                }
                else -> return@setOnTouchListener false
            }

        }
        return@setOnTouchListener false
    }
}

enum class DrawablePositionTypes(val type: Int) {
    START(0),
    TOP(1),
    END(2),
    BOTTOM(3),
    UNEXPECTED(-1);

    companion object {
        fun byValue(value: Int?): DrawablePositionTypes {
            return DrawablePositionTypes.values().firstOrNull { value == it.type }
                    ?: UNEXPECTED
        }
    }

    operator fun invoke() = type

}