package com.ticonsys.hadiths.data.network.middleware

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.ticonsys.hadiths.R
import com.ticonsys.hadiths.utils.ConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptorImpl(
    context: Context
) : ConnectivityInterceptor {
    private val appContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isOnline()){
            throw ConnectivityException(appContext.getString(R.string.txt_internet_connection))
        }
        return chain.proceed(chain.request())
    }

    @Suppress("DEPRECATION")
    private fun isOnline(): Boolean{
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager?
        var hasOnline = false
        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    hasOnline = when{
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            } else {
                val networkInfo = it.activeNetworkInfo
                hasOnline = networkInfo != null && networkInfo.isConnected
            }
        }
        return hasOnline
    }
}