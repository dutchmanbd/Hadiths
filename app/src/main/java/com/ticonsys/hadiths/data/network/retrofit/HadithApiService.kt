package com.ticonsys.hadiths.data.network.retrofit

import androidx.lifecycle.LiveData
import com.ticonsys.hadiths.data.db.entities.Book
import com.ticonsys.hadiths.data.db.entities.Chapter
import com.ticonsys.hadiths.data.network.responses.ApiResponse
import com.ticonsys.hadiths.data.network.responses.ResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HadithApiService {

    @GET("collections")
    fun fetchBooks(
        @Query("limit") limit : Int,
        @Query("page") page: Int
    ): LiveData<ApiResponse<ResponseDTO<List<Book>>>>

    @GET("collections/{collectionName}/books")
    fun fetchChapters(
        @Path("collectionName") collectionName: String
    ): LiveData<ApiResponse<ResponseDTO<List<Chapter>>>>

}