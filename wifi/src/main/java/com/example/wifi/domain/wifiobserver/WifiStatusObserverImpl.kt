package com.example.wifi.domain.wifiobserver

import com.example.wifi.data.ConnectivityDataSource.IConnectivityDataSource
import com.example.wifi.data.WifiInfoDataSource.IWifiInfoDataSource
import com.example.wifi.domain.model.WifiStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class WifiStatusObserverImpl(
    private val connectivity: IConnectivityDataSource,
    private val wifiInfo: IWifiInfoDataSource
) : WifiStatusObserver{

    override fun observe(targetSsid: String): Flow<WifiStatus> {
        val targetNorm = targetSsid.trim().removeSurrounding("\"")

        return connectivity.observeIsWifiActive()
            .map { isWifi ->
                val ssid = if (isWifi) wifiInfo.currentSsidOrNull() else null
                val isTarget = ssid != null && ssid.trim().removeSurrounding("\"") == targetNorm
                WifiStatus(isWifiConnected = isWifi, ssid = ssid, isTargetSsid = isTarget)
            }
            .distinctUntilChanged()
    }
}