package com.activityartapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun stringToMap(value: String): Map<Int, Int> {
        return Gson().fromJson(
            value,
            object : TypeToken<Map<Int, Int>>() {}.type
        )
    }

    @TypeConverter
    fun mapToString(value: Map<Int, Int>?): String {
        return if (value == null) "" else Gson().toJson(value)
    }
}