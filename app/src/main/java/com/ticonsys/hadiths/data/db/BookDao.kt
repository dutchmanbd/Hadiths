package com.ticonsys.hadiths.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ticonsys.hadiths.data.db.entities.Book

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg books: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooks(books: List<Book>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createBookIfNotExists(book: Book): Long


    @Query(
        """
        SELECT * FROM books
        WHERE 1"""
    )
    fun loadBooks(): LiveData<List<Book>>

    @Query(
        """
        SELECT * FROM books
        WHERE name =:name"""
    )
    fun loadBook(name: String): LiveData<Book>

}