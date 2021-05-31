package com.ticonsys.hadiths.utils

import com.ticonsys.hadiths.data.network.responses.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

/**
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 * @param <R>
</R> */
@ExperimentalCoroutinesApi
internal class FlowCallAdapter<R>(
    private val responseType: Type
) : CallAdapter<R, Flow<ApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): Flow<ApiResponse<R>> {
        return callbackFlow {
            call.enqueue(object : Callback<R> {
                override fun onResponse(call: Call<R>, response: Response<R>) {
                    if (!this@callbackFlow.isClosedForSend) {
                        val apiResponse = ApiResponse.create(response)
                        offer(apiResponse)
                    }
                }

                override fun onFailure(call: Call<R>, throwable: Throwable) {
                    offer(ApiResponse.create(throwable))
                }
            })

            awaitClose {
                call.cancel()
            }
        }
    }
}