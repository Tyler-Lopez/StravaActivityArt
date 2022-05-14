package com.company.athleteapiart

import androidx.lifecycle.ViewModel
import com.company.athleteapiart.util.Constants

class MainViewModel() : ViewModel() {

    fun parseYearsFromNav(raw: String?) = (raw ?: "").split(Constants.NAV_DELIMITER)
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
        .toTypedArray()

    fun parseYearMonthsFromNav(raw: String?) = (raw ?: "").split(Constants.NAV_DELIMITER)
        .filter { it.isNotEmpty() }
        .map {
            val year = it.substring(0, 4).toInt()
            val month = it.substring(4).toInt()
            Pair(year, month)
        }.toTypedArray()

    fun parseTypesFromNav(raw: String) = (raw).split(Constants.NAV_DELIMITER)
        .filter { it.isNotEmpty() }
        .map {
            it
        }.toTypedArray()
}