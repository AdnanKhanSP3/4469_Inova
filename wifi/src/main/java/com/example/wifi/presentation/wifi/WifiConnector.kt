package com.example.wifi.presentation.wifi

import android.content.Context
import android.net.*
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import androidx.annotation.RequiresApi

class WifiConnector(private val context: Context) {

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var activeCallback: ConnectivityManager.NetworkCallback? = null
    private var activeNetwork: Network? = null

    /**
     * Connect immediately to a Wi-Fi network (Android 10+).
     *
     * @param ssid Wi-Fi SSID (exact match)
     * @param password WPA2 password, or null for open networks
     * @param onConnected called when connected + bound
     * @param onFailed called if user cancels / timeout / failure
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun connectToWifiNow(
        ssid: String,
        password: String? = null,
        onConnected: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        // Clean up previous request
        disconnect()

        val specifierBuilder = WifiNetworkSpecifier.Builder()
            .setSsid(ssid)

        if (!password.isNullOrEmpty()) {
            specifierBuilder.setWpa2Passphrase(password)
        }

        val specifier = specifierBuilder.build()

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            // IMPORTANT: for Wi-Fi specifier, do NOT require NET_CAPABILITY_INTERNET
            .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .setNetworkSpecifier(specifier)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                activeNetwork = network

                // Route app traffic through this Wi-Fi
                val boundOk = cm.bindProcessToNetwork(network)
                if (!boundOk) {
                    onFailed("bindProcessToNetwork() failed")
                    disconnect()
                    return
                }

                onConnected()
            }

            override fun onUnavailable() {
                onFailed("Network unavailable (user canceled or timeout)")
                disconnect()
            }

            override fun onLost(network: Network) {
                if (activeNetwork == network) {
                    onFailed("Network lost")
                    disconnect()
                }
            }
        }

        activeCallback = callback

        // This will trigger a system UI dialog.
//        cm.requestNetwork(callback = callback, request = request)
        cm.requestNetwork(request, callback)

    }

    /**
     * Disconnect / release the temporary Wi-Fi request and unbind traffic.
     */
    fun disconnect() {
        cm.bindProcessToNetwork(null)

        activeCallback?.let {
            try {
                cm.unregisterNetworkCallback(it)
            } catch (_: Exception) {
                // Ignore if already unregistered
            }
        }

        activeCallback = null
        activeNetwork = null
    }
}
