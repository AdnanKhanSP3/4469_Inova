package com.example.wifi.presentation.wifi

import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WifiManagerHelper
@Inject constructor
    (
    @ApplicationContext  private val context: Context,
     private val wifiManager: WifiManager
            )
{
//    private val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    /**
     * Checks if Wi-Fi is enabled. If it is, verifies connection to the specified SSID.
     * @param targetSSID The SSID of the target Wi-Fi network.
     * @return Boolean indicating if the device is connected to the specified Wi-Fi network.
     * @throws WifiNotEnabledException if Wi-Fi is not enabled.
     * @throws WifiNotConnectedException if not connected to the specified network.
     */

    @Throws(Exception::class)
    fun isConnectedToSpecifiedWifi(targetSSID: String): Boolean {
        // Check if Wi-Fi is enabled
        if (!wifiManager.isWifiEnabled) {
            throw Exception()
        }

        // Get current connected Wi-Fi SSID
        val connectedSSID = wifiManager.connectionInfo.ssid.trim('"') // Trim quotes around SSID on older versions

        val connectedbssid = wifiManager.connectionInfo.bssid


        // Check if connected to the specified SSID
        return if (connectedSSID == targetSSID) {
            true
        } else {
            return  false
        }
    }

    fun suggestNetworkAutoJoin(
        ssid: String,
        password: String? = null
    ): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return false

        val builder = WifiNetworkSuggestion.Builder()
            .setSsid(ssid)
            .setIsAppInteractionRequired(true) // Optional (Needs location permission)

        password?.let {
            builder.setWpa2Passphrase(it)
        }

        val status = wifiManager.addNetworkSuggestions(
            listOf(builder.build())
        )

        return status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS
    }
 }