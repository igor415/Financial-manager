package com.varivoda.igor.tvz.financijskimanager.monitoring

import android.content.Context
import android.net.ConnectivityManager

class ConnectivityAgent(private val context: Context) {

    val isDeviceConnectedToInternet: Boolean
        get() {
            val service = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = service.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }
}