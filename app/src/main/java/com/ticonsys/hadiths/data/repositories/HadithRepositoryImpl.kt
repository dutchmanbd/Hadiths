package com.ticonsys.hadiths.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.ticonsys.hadiths.data.db.BookDao
import com.ticonsys.hadiths.data.db.ChapterDao
import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.data.network.middleware.NetworkBoundResource
import com.ticonsys.hadiths.data.network.responses.ResponseDTO
import com.ticonsys.hadiths.data.network.retrofit.HadithApiService
import com.ticonsys.hadiths.utils.AppExecutors
import com.ticonsys.hadiths.utils.RateLimiter
import com.ticonsys.hadiths.utils.Resource
import java.util.concurrent.TimeUnit

class HadithRepositoryImpl(
    private val appExecutors: AppExecutors,
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao,
    private val apiService: HadithApiService
) : HadithRepository {

    private val booksRateLimit = RateLimiter<String>(15, TimeUnit.MINUTES)
    private val chaptersRateLimit = RateLimiter<String>(15, TimeUnit.MINUTES)

    override fun loadBooks(
        limit: Int, page: Int
    ): LiveData<Resource<List<Book>>> {
        return object : NetworkBoundResource<List<Book>, ResponseDTO<List<Book>>>(appExecutors) {
            override fun saveCallResult(item: ResponseDTO<List<Book>>) {
                item.data ?: return
                bookDao.insertBooks(item.data)
            }

            override fun shouldFetch(data: List<Book>?): Boolean {
                return data == null || data.isEmpty() || booksRateLimit.shouldFetch("$page#$limit")
            }

            override fun loadFromDb() = bookDao.loadBooks()
            override fun createCall() = apiService.fetchBooks(limit, page)
            override fun onFetchFailed() {
                booksRateLimit.reset("$page#$limit")
            }
        }.asLiveData()
    }

    override fun fetchChapters(collectionName: String): LiveData<Resource<List<Chapter>>> {
        return object :
            NetworkBoundResource<List<Chapter>, ResponseDTO<List<Chapter>>>(appExecutors) {
            override fun saveCallResult(item: ResponseDTO<List<Chapter>>) {
                item.data ?: return
                chapterDao.insertChapters(item.data)
            }

            override fun shouldFetch(data: List<Chapter>?): Boolean {
                return data == null || data.isEmpty() || chaptersRateLimit.shouldFetch(
                    collectionName
                )
            }

            override fun loadFromDb() = chapterDao.loadChapters()
            override fun createCall() = apiService.fetchChapters(collectionName)
            override fun onFetchFailed() {
                chaptersRateLimit.reset(collectionName)
            }
        }.asLiveData()
    }
}