package com.example.wifi.data.ConnectivityDataSource

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class ConnectivityDataSourceImpl
    @Inject constructor(
    private val cm: ConnectivityManager
): IConnectivityDataSource
{
    override fun observeIsWifiActive(): Flow<Boolean> = callbackFlow{

        fun isWifiNow(): Boolean {
            val active = cm.activeNetwork
            val caps = cm.getNetworkCapabilities(active)
            return caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        }

        trySend(isWifiNow())

        val cb = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(isWifiNow())
            }
            override fun onLost(network: Network) {
                trySend(isWifiNow())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(isWifiNow())
            }
        }

        cm.registerNetworkCallback(NetworkRequest.Builder().build(), cb)

        awaitClose { runCatching { cm.unregisterNetworkCallback(cb) } }

    }
}