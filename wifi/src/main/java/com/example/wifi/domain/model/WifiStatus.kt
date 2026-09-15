package com.example.wifi.domain.model


data class WifiStatus(
    val isWifiConnected: Boolean,
    val ssid: String?,
    val isTargetSsid: Boolean
)