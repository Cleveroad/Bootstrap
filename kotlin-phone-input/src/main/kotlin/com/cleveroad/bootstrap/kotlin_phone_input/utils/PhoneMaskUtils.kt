package com.cleveroad.bootstrap.kotlin_phone_input.utils


object PhoneMaskUtils {

    fun generatePhoneMask(phoneFormat: String, ignoreCountryCode: Boolean = false) =
            splitToBlocks(phoneFormat.trim())
                    .removeFirstBlockIfCodeIgnore(ignoreCountryCode)
                    .addSquareBracketsForBlocks()
                    .addRoundBracketsForCode()
                    .buildMask()
}

private fun splitToBlocks(phoneFormat: String): Pair<MutableList<String>, MutableList<Char>> {
    val blocks = mutableListOf<String>()
    val dividers = mutableListOf<Char>()
    var block = StringBuilder()
    phoneFormat.forEachIndexed { i, ch ->
        if (ch == '+') {
            dividers.add(ch)
        } else if (ch == ' ' || ch == '-') {
            dividers.add(ch)
            blocks.add(block.toString())
            block = StringBuilder()
        } else {
            block.append('0')
        }
        if (i == phoneFormat.length - 1) {
            blocks.add(block.toString())
        }
    }
    return Pair(blocks, dividers)
}

private fun Pair<MutableList<String>, MutableList<Char>>.removeFirstBlockIfCodeIgnore(ignoreCode: Boolean) =
        apply {
            if (ignoreCode && second.contains('+')) {
                first.removeAt(0)
                second.removeAt(0)
                second.removeAt(0)
            }
        }

private fun Pair<MutableList<String>, MutableList<Char>>.addSquareBracketsForBlocks() =
        apply {
            val blocks = mutableListOf<String>()
            first.forEach { blocks.add("[$it]") }
            return Pair(blocks, second)
        }

private fun Pair<MutableList<String>, MutableList<Char>>.addRoundBracketsForCode() =
        apply {
            if (first.size > 1) {
                var ind = 0
                if (second.contains('+')) ind = 1
                first[ind] = "(${first[ind]})"
            }
        }

private fun Pair<MutableList<String>, MutableList<Char>>.buildMask(): String {
    val firstArray: MutableList<*>
    val secondArray: MutableList<*>
    if (second.contains('+')) {
        firstArray = second
        secondArray = first
    } else {
        firstArray = first
        secondArray = second
    }
    return StringBuilder().apply {
        firstArray.forEachIndexed { index, element ->
            append(element)
            if (secondArray.size > index) append(secondArray[index])
        }
    }.toString()
}