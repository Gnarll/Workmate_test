package com.example.workmate_test.domain.utils

sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    object Loading : Result<Nothing>
    sealed class Error(val e: Throwable) : Result<Nothing> {
        class NetworkError(e: Throwable) : Error(e)
        class ServerError(e: Throwable) : Error(e)
        class SerializationError(e: Throwable) : Error(e)
        class UnknownError(e: Throwable) : Error(e)
    }
}

