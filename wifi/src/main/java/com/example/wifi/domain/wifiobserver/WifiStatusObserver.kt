package com.example.wifi.domain.wifiobserver

import com.example.wifi.domain.model.WifiStatus
import kotlinx.coroutines.flow.Flow

interface WifiStatusObserver {
    fun observe(targetSsid: String): Flow<WifiStatus>
}