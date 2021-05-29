package com.ticonsys.hadiths.data.repositories

import androidx.lifecycle.LiveData
import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.utils.Resource

interface HadithRepository {

    fun loadBooks(limit: Int, page: Int): LiveData<Resource<List<Book>>>

    fun fetchChapters(collectionName: String): LiveData<Resource<List<Chapter>>>
}