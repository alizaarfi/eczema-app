package com.example.mob_dev_portfolio

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isBlank()) emptyList() else value.split(",")
    }

    @TypeConverter
    fun fromBodyAreaMap(value: Map<String, Int>?): String {
        if (value == null) return "{}"
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toBodyAreaMap(value: String): Map<String, Int> {
        if (value.isBlank() || value == "{}") return emptyMap()
        val type = object : TypeToken<Map<String, Int>>() {}.type
        return Gson().fromJson(value, type)
    }
}