package com.example.core_ui.preferenceDataStore.domain.repositoy

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveAppName(name: String)
    fun getAppName(): Flow<String>

    suspend fun saveIpAddress(ipAddress: String)
    fun getIpAddress(): Flow<String>

    suspend fun savePortNumber(port: String)
    fun getPortNmber(): Flow<String>

    suspend fun saveShowCase(value: Boolean)
    fun getShowCase(): Flow<Boolean>
}
