package com.ticonsys.hadiths.data.network.middleware

import com.ticonsys.hadiths.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptorImpl() : AuthInterceptor {
    companion object{
        private const val TAG = "AuthInterceptorImpl"
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authenticationRequest = request(originalRequest)
        return chain.proceed(authenticationRequest)
    }
    private fun request(originalRequest: Request): Request {
        return originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("X-API-Key", BuildConfig.API_KEY)
            .build()
    }

}