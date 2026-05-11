package com.example.helperjc.data.network

sealed class AsyncResult<out T> {
    data class Error(val errorMessage: String) : AsyncResult<Nothing>()
    data class Success<out T>(val data: T) : AsyncResult<T>()
}