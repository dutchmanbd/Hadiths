package com.ticonsys.hadiths.data.db.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.data.db.entities.Hadith

data class ChapterWithHadith(
    @Embedded
    val chapter: Chapter,
    @Relation(
        parentColumn = "bookNumber",
        entityColumn = "bookNumber"
    )
    val hadith: List<Hadith>
)