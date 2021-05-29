package com.ticonsys.hadiths.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ticonsys.hadiths.data.db.converters.CollectionTypeConverters

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val hasBooks: Boolean,
    val hasChapters: Boolean,
    val collection: List<Collection>,
    val totalHadith: Int,
    val totalAvailableHadith: Int
){
    data class Collection(
        val lang: String,
        val shortIntro: String,
        val title: String
    )
}