package com.ticonsys.hadiths.data.network.retrofit


import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.data.db.entities.Hadith
import com.ticonsys.hadiths.data.network.responses.ApiResponse
import com.ticonsys.hadiths.data.network.responses.ResponseDTO
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HadithApiService {

    @GET("collections")
    fun fetchBooks(
        @Query("limit") limit : Int,
        @Query("page") page: Int
    ): Flow<ApiResponse<ResponseDTO<List<Book>>>>

    @GET("collections/{collectionName}/books")
    fun fetchChapters(
        @Path("collectionName") collectionName: String
    ): Flow<ApiResponse<ResponseDTO<List<Chapter>>>>

    @GET("collections/{collectionName}/books/{bookNumber}/hadiths")
    fun getHadith(
        @Path("collectionName") collectionName: String,
        @Path("bookNumber") bookNumber: String
    ): Flow<ApiResponse<ResponseDTO<List<Hadith>>>>

    @GET("collections/{collectionName}/hadiths/{hadithNumber}")
    fun getHadithDetail(
        @Path("collectionName") collectionName: String,
        @Path("hadithNumber") hadithNumber: String
    ): Flow<ApiResponse<ResponseDTO<Hadith>>>

}