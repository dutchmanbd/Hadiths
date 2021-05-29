package com.ticonsys.hadiths.data.db.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.ticonsys.hadiths.data.db.converters.TitleTypeConverters

@Entity(tableName = "chapters")
data class Chapter(
    @PrimaryKey(autoGenerate = false)
    val bookNumber: String,
    @field:SerializedName("book")
    val titles: List<Title>,
    val hadithEndNumber: Int,
    val hadithStartNumber: Int,
    val numberOfHadith: Int
){
    data class Title(
        val lang: String,
        val name: String
    )
}