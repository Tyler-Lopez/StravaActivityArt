package com.company.activityart.util

class NavigationUtils {

    companion object {
        // 1..2 ---> 1~2
        fun distanceNavArgs(distanceRange: ClosedFloatingPointRange<Float>) = buildString {
            append(distanceRange.start)
                .append(Constants.NAV_DELIMITER)
                .append(distanceRange.endInclusive)
        }

        fun yearMonthsNavArgs(yearMonths: Array<Pair<Int, Int>>) = buildString {
            yearMonths.forEach {
                append(it.first).append(it.second)
                    .append(Constants.NAV_DELIMITER)
            }
        }

        fun activityTypesNavArgs(activityTypes: Array<String>?) = buildString {
            activityTypes?.forEach {
                append(it).append(Constants.NAV_DELIMITER)
            }
        }

        fun gearsNavArgs(gears: Array<String?>?) = buildString {
            gears?.forEach {
                append(it).append(Constants.NAV_DELIMITER)
            }
        }
    }

}