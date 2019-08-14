package com.martin.stackusers.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.martin.stackusers.models.Badge

class Converters {
    @TypeConverter
    fun badgeToString(badge: Badge): String {
        return Gson().toJson(badge)
    }

    @TypeConverter
    fun stringToBadge(data: String): Badge {
        return Gson().fromJson(data, Badge::class.java)
    }
}