package com.ticonsys.hadiths.data.db

import androidx.room.*
import com.ticonsys.hadiths.data.db.entities.Hadith
import com.ticonsys.hadiths.data.db.entities.relations.ChapterWithHadith
import kotlinx.coroutines.flow.Flow

@Dao
interface HadithDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg hadith: Hadith)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHadith(hadith: List<Hadith>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createHadithIfNotExists(hadith: Hadith): Long

    @Query(
        """
        SELECT * FROM hadith
        WHERE bookNumber = :bookNumber"""
    )
    fun loadHadithList(bookNumber: String): Flow<List<Hadith>>

    @Query(
        """
        SELECT * FROM hadith
        WHERE hadithNumber = :hadithNumber"""
    )
    fun loadHadith(hadithNumber: String): Flow<Hadith>


    @Transaction
    @Query("SELECT * FROM chapters WHERE bookNumber = :bookNumber")
    fun getSchoolWithStudents(bookNumber: String): Flow<List<ChapterWithHadith>>
}