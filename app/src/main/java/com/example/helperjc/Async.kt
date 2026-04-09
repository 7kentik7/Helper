package com.example.helperjc

sealed class Async<out T> {
    data class Error(val errorMessage: Int) : Async<Nothing>()

    data class Success<out T>(val data: T) : Async<T>()

    data object Loading : Async<Nothing>()
}