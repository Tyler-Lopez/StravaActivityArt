package com.activityartapp.util

class ParseNumberFromStringUtils {

    companion object {
        private const val DEFAULT_ERROR_VALUE = 0.0
        private const val DECIMAL_SEPARATOR = '.'
        private const val NEGATIVE_INDICATOR = '-'
    }

    fun parse(string: String): Double {
        return string
            .filter { it in '0'..'9' || it == DECIMAL_SEPARATOR || it == NEGATIVE_INDICATOR }
            .fold(StringBuilder()) { total, new ->
                if ((new == NEGATIVE_INDICATOR && (total.contains(NEGATIVE_INDICATOR) || total.lastIndex != -1)) ||
                    (new == DECIMAL_SEPARATOR && total.contains(DECIMAL_SEPARATOR))
                ) {
                    total
                } else {
                    total.append(new)
                }
            }
            .toString()
            .toDoubleOrNull()
            ?: DEFAULT_ERROR_VALUE
    }
}