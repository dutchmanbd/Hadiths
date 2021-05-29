package com.ticonsys.hadiths.data.network.middleware

import com.ticonsys.hadiths.utils.ConnectivityException
import com.zxdmjr.material_utils.network.ERROR_UNKNOWN
import com.zxdmjr.material_utils.network.Resource
import org.json.JSONException
import retrofit2.Response


abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.Failed(null, ERROR_UNKNOWN)
                }
            } else {
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    Resource.Failed(errorBody, "")
                } else {
                    Resource.Failed(null, response.message())
                }
            }
        } catch (e: JSONException) {
            Resource.Failed(null, e.message ?: ERROR_UNKNOWN)
        } catch (e: Exception) {
            Resource.Failed(null, e.message ?: ERROR_UNKNOWN)
        } catch (e: ConnectivityException) {
            Resource.Failed(null, e.message ?: ERROR_UNKNOWN)
        }

    }

}