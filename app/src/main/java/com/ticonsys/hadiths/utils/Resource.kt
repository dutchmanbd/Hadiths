package com.ticonsys.hadiths.utils

import okhttp3.ResponseBody


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
//data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
//    companion object {
//        fun <T> success(data: T?): Resource<T> {
//            return Resource(Status.SUCCESS, data, null)
//        }
//
//        fun <T> error(msg: String, data: T?): Resource<T> {
//            return Resource(Status.ERROR, data, msg)
//        }
//
//        fun <T> loading(data: T?): Resource<T> {
//            return Resource(Status.LOADING, data, null)
//        }
//    }
//}

sealed class Resource<out T> {
    data class Success<out T>(val data: T): Resource<T>()
    data class Loading<out T>(val data: T?): Resource<T>()
    data class Error<out T>(val msg: String, val data: T?): Resource<T>()
}