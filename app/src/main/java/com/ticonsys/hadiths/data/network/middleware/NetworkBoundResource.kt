import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.ticonsys.hadiths.data.network.responses.ApiEmptyResponse
import com.ticonsys.hadiths.data.network.responses.ApiErrorResponse
import com.ticonsys.hadiths.data.network.responses.ApiResponse
import com.ticonsys.hadiths.data.network.responses.ApiSuccessResponse
import com.ticonsys.hadiths.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.withContext

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
@FlowPreview
@ExperimentalCoroutinesApi
abstract class NetworkBoundResource<ResultType, RequestType> {
    suspend fun asFlow(): Flow<Resource<ResultType>> {
        return loadFromDb().transformLatest { dbValue ->
            if (shouldFetch(dbValue)) {
                emit(Resource.loading(dbValue))

                createCall().collect { apiResponse ->
                    when (apiResponse) {
                        is ApiSuccessResponse -> {
                            withContext(Dispatchers.IO) {
                                saveCallResult(processResponse(apiResponse))
                            }
                        }

                        is ApiEmptyResponse -> {
                            emit(Resource.success(dbValue))
                        }

                        is ApiErrorResponse -> {
                            onFetchFailed()
                            emit(Resource.error(apiResponse.errorMessage, dbValue))
                        }
                    }
                }

            } else {
                emit(Resource.success(dbValue))
            }
        }
    }

    protected open fun onFetchFailed() {
        // Implement in sub-classes to handle errors
    }

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract suspend fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): Flow<ResultType?>

    @MainThread
    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>
}