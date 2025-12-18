package com.example.workmate_test.data.utils

sealed class ResultWrapper<out T>() {
    object Empty : ResultWrapper<Nothing>()
    class Existent<T>(val result: T) : ResultWrapper<T>()
}