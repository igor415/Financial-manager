package com.varivoda.igor.tvz.financijskimanager.monitoring

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build

class NetworkChangeReceiver(
    private val connectivityManager: ConnectivityManager
) {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            isConnected = true
        }

        override fun onLost(network: Network?) {
            isConnected = false

        }
    }

    fun startObservingNetwork() {
        val builder = NetworkRequest.Builder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    fun stopObservingNetwork() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    companion object {
        var isConnected = false

    }
}