package com.ticonsys.hadiths.data.repositories

import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.data.db.entities.Hadith
import com.ticonsys.hadiths.utils.Resource
import kotlinx.coroutines.flow.Flow


interface HadithRepository {

    suspend fun loadBooks(limit: Int, page: Int): Flow<Resource<List<Book>>>

    suspend fun fetchChapters(collectionName: String): Flow<Resource<List<Chapter>>>

    suspend fun getHadith(collectionName: String, bookNumber: String): Flow<Resource<List<Hadith>>>

    suspend fun getHadithDetail(collectionName: String, hadithNumber: String): Flow<Resource<Hadith>>
}