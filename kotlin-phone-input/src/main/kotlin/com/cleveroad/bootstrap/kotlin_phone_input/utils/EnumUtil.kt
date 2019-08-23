package com.cleveroad.bootstrap.kotlin_phone_input.utils

import androidx.annotation.StringRes
import com.cleveroad.bootstrap.kotlin_phone_input.R

/**
 * Get file path for [R.styleable.PhoneView_pcv_flag_shape]
 *
 * @param value from which corresponds to [R.styleable.PhoneView_pcv_flag_shape]
 *
 * @return resource reference[StringRes] which contains flag path
 */
@StringRes
fun getFlagPath(value: Int): Int? = when (value) {
    0 -> R.string.path_heart
    1 -> R.string.path_circle
    2 -> R.string.path_circle_airplane
    3 -> R.string.path_circle_pin
    4 -> R.string.path_square
    5 -> R.string.path_square_round
    6 -> R.string.path_rhombus
    7 -> R.string.path_triangle
    8 -> R.string.path_chat
    9 -> R.string.path_chat_2
    10 -> R.string.path_pin
    11 -> R.string.path_pin_big
    12 -> R.string.path_cluster_pin
    13 -> R.string.path_star
    14 -> R.string.path_star_round
    15 -> R.string.path_house
    16 -> R.string.path_house_2
    17 -> R.string.path_compass_ahead
    18 -> R.string.path_compass_top_right
    19 -> R.string.path_backward
    20 -> R.string.path_man
    21 -> R.string.path_email
    22 -> R.string.path_cloud
    23 -> R.string.path_phone
    24 -> R.string.path_play
    25 -> R.string.path_oil
    else -> null
}

enum class State(val value: Int) {
    ACTIVE(0),
    INACTIVE(1),
    ERROR(2),
    ERROR_INACTIVE(3),
    VALID(4);

    companion object {
        fun getState(state: Int) =
                values().firstOrNull { state == it.value } ?: ACTIVE
    }
}
