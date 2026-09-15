package com.example.wifi.data.ConnectivityDataSource

import kotlinx.coroutines.flow.Flow

interface IConnectivityDataSource {
    fun observeIsWifiActive(): Flow<Boolean>

}