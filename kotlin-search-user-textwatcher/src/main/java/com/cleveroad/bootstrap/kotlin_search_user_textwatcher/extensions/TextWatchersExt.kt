package com.cleveroad.bootstrap.kotlin_search_user_textwatcher.extensions

import com.cleveroad.bootstrap.kotlin_search_user_textwatcher.*

/**
 * Check if character [Char] belongs to one of the types: [SPACE_CHARACTER], [DOT_CHARACTER],
 * [QUESTION_CHARACTER], [EXCLAMATION_CHARACTER].
 */
fun Char.isSpecialCharacter() = this == SPACE_CHARACTER
        || this == DOT_CHARACTER
        || this == COMMA_CHARACTER
        || this == QUESTION_CHARACTER
        || this == EXCLAMATION_CHARACTER
