package com.varivoda.igor.tvz.financijskimanager.util

sealed class NetworkResult<out T : Any> {

    data class Loading<out T : Any>(val data: T?) : NetworkResult<T>()
    data class Success<out T : Any>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
    data class NoNetworkConnection(val msg: String) : NetworkResult<Nothing>()

}