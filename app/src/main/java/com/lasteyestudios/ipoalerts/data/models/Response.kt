package com.lasteyestudios.ipoalerts.data.models

// A generic class to send status and response via flow
sealed class Response<out R> {
    data class Success<out T>(val data: T) : Response<T>()
    object Loading : Response<Nothing>()
    object Error : Response<Nothing>()
}