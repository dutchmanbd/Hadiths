package com.ticonsys.hadiths.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "hadith")
data class Hadith(
    @PrimaryKey(autoGenerate = false)
    val hadithNumber: String,
    val bookNumber: String,
    val chapterId: String,
    val collection: String,
    @field:SerializedName("hadith")
    val details: List<Detail>
) {
    data class Detail(
        val lang: String,
        val body: String,
        val chapterNumber: String,
        val chapterTitle: String,
        val grades: List<Grade> = emptyList()
    ){
        data class Grade(
            val grade: String,
            val gradedBy: String
        )
    }


}
