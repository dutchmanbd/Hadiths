package com.ticonsys.hadiths.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ticonsys.hadiths.data.db.entities.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg books: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createBookIfNotExists(book: Book): Long


    @Query(
        """
        SELECT * FROM books
        WHERE 1"""
    )
    fun loadBooks(): Flow<List<Book>?>

    @Query(
        """
        SELECT * FROM books
        WHERE name =:name"""
    )
    fun loadBook(name: String): LiveData<Book>

}