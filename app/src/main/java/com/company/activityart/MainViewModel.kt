package com.company.activityart

import androidx.lifecycle.ViewModel
import com.company.activityart.util.Constants

class MainViewModel : ViewModel() {

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

    fun parseTypesFromNav(raw: String?) =
        raw?.split(Constants.NAV_DELIMITER)?.filter { it.isNotEmpty() }?.map {
            it
        }?.toTypedArray()

    fun parseGearsFromNav(raw: String?) =
        raw?.split(Constants.NAV_DELIMITER)?.filter { it.isNotEmpty() }?.map { gearId ->
            if (gearId == "null")
                null
            else gearId
        }?.toTypedArray()

    fun parseDistancesFromNav(raw: String?): ClosedFloatingPointRange<Float>? {
        val arr = raw?.split(Constants.NAV_DELIMITER)?.filter { it.isNotEmpty() }
        return arr?.let {
            it[0].toFloat()..it[1].toFloat()
        }
    }
}