package com.ticonsys.hadiths.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ticonsys.hadiths.data.db.entities.Hadith
import com.ticonsys.hadiths.utils.fromJson

object DetailTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToDetailList(data: String?): List<Hadith.Detail>? {
        return data?.let {
            try {
                Gson().fromJson<List<Hadith.Detail>>(it)
            } catch (ex: Exception) {
                null
            }
        }
    }

    @TypeConverter
    @JvmStatic
    fun detailListToString(grades: List<Hadith.Detail>?): String? {
        return Gson().toJson(grades)
    }

}