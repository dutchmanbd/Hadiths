package com.ticonsys.hadiths.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.utils.fromJson

object CollectionTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToCollectionList(data: String?): List<Book.Collection>? {
        return data?.let {
            try {
                Gson().fromJson<List<Book.Collection>>(it)
            } catch (ex: Exception) {
                null
            }
        }
    }

    @TypeConverter
    @JvmStatic
    fun collectionListToString(collections: List<Book.Collection>?): String? {
        return Gson().toJson(collections)
    }


}