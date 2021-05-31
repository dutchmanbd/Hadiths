package com.ticonsys.hadiths.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ticonsys.hadiths.data.db.entities.Chapter
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg chapters: Chapter)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChapters(chapters: List<Chapter>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createChapterIfNotExists(chapter: Chapter): Long


    @Query(
        """
        SELECT * FROM chapters
        WHERE 1"""
    )
    fun loadChapters(): Flow<List<Chapter>>

    @Query(
        """
        SELECT * FROM chapters
        WHERE bookNumber =:bookNumber"""
    )
    fun loadChapter(bookNumber: String): Flow<Chapter>
}