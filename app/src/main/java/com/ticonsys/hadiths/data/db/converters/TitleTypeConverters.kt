package com.ticonsys.hadiths.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.utils.fromJson

object TitleTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToTitleList(data: String?): List<Chapter.Title>? {
        return data?.let {
            try {
                Gson().fromJson<List<Chapter.Title>>(it)
            } catch (ex: Exception) {
                emptyList()
            }
        }
    }

    @TypeConverter
    @JvmStatic
    fun titleListToString(titles: List<Chapter.Title>?): String? {
        return Gson().toJson(titles)
    }


}