package com.ticonsys.hadiths.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ticonsys.hadiths.data.db.converters.CollectionTypeConverters
import com.ticonsys.hadiths.data.db.converters.TitleTypeConverters
import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.data.db.entities.Chapter


const val HADITH_DATABASE_NAME = "hadith.db"
@Database(
    entities = [Book::class, Chapter::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CollectionTypeConverters::class, TitleTypeConverters::class)
abstract class HadithDatabase : RoomDatabase(){
    abstract fun getBookDao(): BookDao
    abstract fun getChapterDao(): ChapterDao
}