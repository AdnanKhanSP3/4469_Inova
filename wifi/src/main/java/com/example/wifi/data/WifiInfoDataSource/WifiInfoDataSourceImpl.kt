package com.example.wifi.data.WifiInfoDataSource

import android.net.wifi.WifiManager
import javax.inject.Inject

class WifiInfoDataSourceImpl
@Inject constructor(
    private val wifiManager: WifiManager
): IWifiInfoDataSource {
    override fun currentSsidOrNull(): String? {

        val raw = wifiManager.connectionInfo?.ssid ?: return null
        val cleaned = raw.removeSurrounding("\"")
        if (cleaned.equals("unknown ssid", ignoreCase = true)) return null
        if (cleaned.isBlank()) return null
        return cleaned
    }
}