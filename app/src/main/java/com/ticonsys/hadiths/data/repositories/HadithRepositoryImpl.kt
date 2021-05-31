package com.ticonsys.hadiths.data.repositories

import NetworkBoundResource
import com.ticonsys.hadiths.data.db.BookDao
import com.ticonsys.hadiths.data.db.ChapterDao
import com.ticonsys.hadiths.data.db.HadithDao
import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.data.db.entities.Hadith
import com.ticonsys.hadiths.data.network.responses.ApiResponse
import com.ticonsys.hadiths.data.network.responses.ResponseDTO
import com.ticonsys.hadiths.data.network.retrofit.HadithApiService
import com.ticonsys.hadiths.utils.RateLimiter
import com.ticonsys.hadiths.utils.Resource
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class HadithRepositoryImpl(
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao,
    private val hadithDao: HadithDao,
    private val apiService: HadithApiService
) : HadithRepository {

    private val booksRateLimit = RateLimiter<String>(15, TimeUnit.MINUTES)
    private val chaptersRateLimit = RateLimiter<String>(15, TimeUnit.MINUTES)
    private val hadithRateLimit = RateLimiter<String>(15, TimeUnit.MINUTES)
    private val hadithDetailRateLimit = RateLimiter<String>(15, TimeUnit.MINUTES)

    override suspend fun loadBooks(limit: Int, page: Int): Flow<Resource<List<Book>>> {
        return object : NetworkBoundResource<List<Book>, ResponseDTO<List<Book>>>() {
            override suspend fun saveCallResult(item: ResponseDTO<List<Book>>) {
                bookDao.insertBooks(item.data ?: emptyList())
            }

            override fun shouldFetch(data: List<Book>?): Boolean {
                return data == null || data.isEmpty() || booksRateLimit.shouldFetch("$page#$limit")
            }

            override fun loadFromDb() = bookDao.loadBooks()

            override suspend fun createCall() = apiService.fetchBooks(limit, page)

            override fun onFetchFailed() {
                booksRateLimit.reset("$page#$limit")
            }
        }.asFlow()
    }


    override suspend fun fetchChapters(collectionName: String): Flow<Resource<List<Chapter>>> {
        return object : NetworkBoundResource<List<Chapter>, ResponseDTO<List<Chapter>>>() {
            override suspend fun saveCallResult(item: ResponseDTO<List<Chapter>>) {
                chapterDao.insertChapters(item.data ?: emptyList())
            }

            override fun shouldFetch(data: List<Chapter>?): Boolean {
                return data == null || data.isEmpty() || chaptersRateLimit.shouldFetch(
                    collectionName
                )
            }

            override fun loadFromDb() = chapterDao.loadChapters()

            override suspend fun createCall() = apiService.fetchChapters(collectionName)

            override fun onFetchFailed() {
                chaptersRateLimit.reset(collectionName)
            }

        }.asFlow()
    }

    override suspend fun getHadith(
        collectionName: String,
        bookNumber: String
    ): Flow<Resource<List<Hadith>>> {
        return object : NetworkBoundResource<List<Hadith>, ResponseDTO<List<Hadith>>>() {
            override suspend fun saveCallResult(item: ResponseDTO<List<Hadith>>) {
                val hadith = item.data ?: emptyList()
                hadithDao.insertHadith(hadith)
            }

            override fun shouldFetch(data: List<Hadith>?): Boolean {
                return data == null || data.isEmpty() || hadithRateLimit.shouldFetch(
                    bookNumber
                )
            }

            override fun loadFromDb() = hadithDao.loadHadithList(bookNumber)
            override suspend fun createCall() = apiService.getHadith(collectionName, bookNumber)

            override fun onFetchFailed() {
                hadithRateLimit.reset(bookNumber)
            }
        }.asFlow()
    }

    override suspend fun getHadithDetail(
        collectionName: String,
        hadithNumber: String
    ): Flow<Resource<Hadith>> {
        return object : NetworkBoundResource<Hadith, ResponseDTO<Hadith>>() {
            override suspend fun saveCallResult(item: ResponseDTO<Hadith>) {
                item.data ?: return
                hadithDao.insert(item.data)
            }
            override fun shouldFetch(data: Hadith?): Boolean {
                return data == null || hadithDetailRateLimit.shouldFetch(
                    hadithNumber
                )
            }
            override fun loadFromDb() = hadithDao.loadHadith(hadithNumber)

            override suspend fun createCall() =
                apiService.getHadithDetail(collectionName, hadithNumber)

            override fun onFetchFailed() {
                hadithDetailRateLimit.reset(hadithNumber)
            }
        }.asFlow()
    }


    /*override fun loadBooks(limit: Int, page: Int): LiveData<Resource<List<Book>>> = liveData {
        emit(Resource.Loading())
        emit(
            if (booksRateLimit.shouldFetch("$page#$limit")) {
                fetchBookFromNetwork(limit, page)
            } else {
                loadBooksFromDB()
            }
        )
    }

    private suspend fun fetchBookFromNetwork(limit: Int, page: Int): Resource<List<Book>> {
        return when (val resource = dataSource.fetchBooks(limit, page)) {
            is Resource.Success -> {
                persistBooks(resource.data.data)
                loadBooksFromDB()
            }
            is Resource.Loading -> {
                Resource.Loading()
            }
            is Resource.Error -> {
                booksRateLimit.reset("$page#$limit")
                loadBooksFromDB()
            }
        }
    }

    private suspend fun persistBooks(books: List<Book>?) {
        bookDao.insertBooks(books ?: emptyList())
    }

    private suspend fun loadBooksFromDB(): Resource<List<Book>> {
        val books = bookDao.loadBooks()
        return if (books != null)
            Resource.Success(books)
        else
            Resource.Error("No data available")
    }

    */

}