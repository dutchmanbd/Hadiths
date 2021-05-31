package com.ticonsys.hadiths.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ticonsys.hadiths.data.db.entities.Hadith
import com.ticonsys.hadiths.utils.fromJson

object GradeTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToGradeList(data: String?): List<Hadith.Detail.Grade>? {
        return data?.let {
            try {
                Gson().fromJson<List<Hadith.Detail.Grade>>(it)
            } catch (ex: Exception) {
                null
            }
        }
    }

    @TypeConverter
    @JvmStatic
    fun gradeListToString(grades: List<Hadith.Detail.Grade>?): String? {
        return Gson().toJson(grades)
    }
}