package com.company.athleteapiart.util

fun timeToString(input: Int): String {
    val hours = input / 3600
    val minutes = (input % 3600) / 60
    val seconds = (input % 3600) % 60

    val toReturn = StringBuilder()
    if (hours != 0) toReturn.append("${hours}h ")
    if (minutes != 0 || hours != 0) toReturn.append("${minutes}m ")
    if (hours == 0) toReturn.append("${seconds}s")
    return toReturn.toString()
}