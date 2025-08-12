package com.kaizencoder.newzify.data

sealed class DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>()
    object NetworkError : DataResult<Nothing>()
    object CacheError : DataResult<Nothing>()
    data class GenericError(val message: String) : DataResult<Nothing>()
    object UnknownError : DataResult<Nothing>()

}
