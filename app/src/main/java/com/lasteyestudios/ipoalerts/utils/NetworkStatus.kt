package com.lasteyestudios.ipoalerts.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest


// Callback for network changes
class NetworkStatus(context: Context) {

    private val connectivityManager = context.applicationContext
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    fun startNetworkCallback(myCallback : (Boolean) -> Unit) {
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(
            builder.build(),
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    myCallback(true)
                }
                override fun onLost(network: Network) {
                    myCallback(false)
                }
            })
    }

//    fun getCurrentNetworkStatus() : Boolean {
//        val activeNetwork = connectivityManager
//            .getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
//        return when {
//            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//            else -> false
//        }
//    }

//    fun stopNetworkCallback() {
//        val connectivityManager: ConnectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        connectivityManager.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
//    }
}