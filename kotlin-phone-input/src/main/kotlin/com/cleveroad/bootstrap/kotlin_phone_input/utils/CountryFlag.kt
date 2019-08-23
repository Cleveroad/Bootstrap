package com.cleveroad.bootstrap.kotlin_phone_input.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

object CountryFlag {
    fun getCountryFlagByCode(context: Context, code: String): Drawable? {
        return try {
            ContextCompat.getDrawable(context,
                    getCountryFlagResIdByCode(context, code))
        } catch (resourceNotFoundException: Resources.NotFoundException) {
            Log.w(javaClass.simpleName, resourceNotFoundException)
            null
        }
    }

    @DrawableRes
    fun getCountryFlagResIdByCode(context: Context, code: String): Int {
        context.run {
            val resId = resources.getIdentifier("ic_list_${code.toLowerCase()}", "drawable", packageName)
            return if (resId != 0) resId
            else resources.getIdentifier("ic_list_unknown", "drawable", packageName)
        }
    }
}