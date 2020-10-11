package com.varivoda.igor.tvz.financijskimanager.util

sealed class Result<out T : Any> {

    data class Loading<out T : Any>(val data: T?) : Result<T>()
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class NoNetworkConnection(val msg: String) : Result<Nothing>()

}