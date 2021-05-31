package com.ticonsys.hadiths.data.network.middleware

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.ticonsys.hadiths.data.network.responses.ApiEmptyResponse
import com.ticonsys.hadiths.data.network.responses.ApiErrorResponse
import com.ticonsys.hadiths.data.network.responses.ApiResponse
import com.ticonsys.hadiths.data.network.responses.ApiSuccessResponse
import com.ticonsys.hadiths.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest

/**
 * A generic class that can provide a resource straight from the network
 * It skips caching like NetworkBoundResource, in cases where there is no need to store
 * locally the data fetched from the network. The flow-object can still be able to capture
 * the data last emitted based flow.onComplete{}.
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <RequestType>
</RequestType></ResultType> */
@FlowPreview
@ExperimentalCoroutinesApi
abstract class NetworkResource<RequestType> {
    suspend fun asFlow(): Flow<Resource<RequestType>> {
        return createCall().transformLatest { apiResponse ->
            when (apiResponse) {
                is ApiSuccessResponse -> {
                    emit(Resource.success(processResponse(apiResponse)))
                }

                is ApiEmptyResponse -> {
                    emit(Resource.success(null))
                }

                is ApiErrorResponse -> {
                    onFetchFailed()
                    emit(Resource.error(apiResponse.errorMessage, null))
                }
            }
        }.onStart {
            emit(Resource.loading(null))
        }
    }

    protected open fun onFetchFailed() {
        // Implement in sub-classes to handle errors
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @MainThread
    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>
}