package com.todo.utils.networkhelper

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

/**
 * This class monitors the internet connection of the device and returns a LiveData
 * of type Boolean.This LiveData is observed in the MainActivity to display
 * necessary notification.
 */
class NetworkStatusHelper(private val context: Context): LiveData<NetworkStatus>() {

    var connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback
    val validNetworkConnections: ArrayList<Network> = ArrayList()



    fun getConnectivityCallbacks() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val networkCapability =
                    connectivityManager.getNetworkCapabilities(network)
                val hasNetworkConnection =
                    networkCapability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        ?: false

                if (hasNetworkConnection) {
                    determineInternetAccess(network)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                validNetworkConnections.remove(network)
                announceNetworkStatus()
            }

            private fun determineInternetAccess(network: Network) {

                CoroutineScope(Dispatchers.IO).launch {
                    if (InternetAvailability.check()) {
                        withContext(Dispatchers.Main) {
                            validNetworkConnections.add(network)

                            announceNetworkStatus()
                        }
                    }
                }
            }


            fun announceNetworkStatus() {

                if (validNetworkConnections.isNotEmpty()) {
                    postValue(NetworkStatus.Available)
                } else {
                    postValue(NetworkStatus.Unavailable)
                }
            }

        }
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }


    override fun onActive() {
        super.onActive()
        connectivityManagerCallback = getConnectivityCallbacks()
        val networkRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            NetworkRequest
                .Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }


    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        }
    }

    object InternetAvailability {

        fun check() : Boolean {
            return try {
                val socket = Socket()
                socket.connect(InetSocketAddress("8.8.8.8",53))
                socket.close()
                true
            } catch ( e: Exception){
                e.printStackTrace()
                false
            }
        }

    }
}

