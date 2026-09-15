package com.example.core_ui.preferenceDataStore.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.commonresources.R
import com.example.core_ui.preferenceDataStore.DataStoreKeys
import com.example.core_ui.preferenceDataStore.dataStore
import com.example.core_ui.preferenceDataStore.domain.repositoy.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.core_ui.admin.presentation.component.utils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SettingsRepositoryImpl
@Inject constructor
    (@ApplicationContext val context: Context) : SettingsRepository {

    override suspend fun saveAppName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.APP_NAME_KEY] = name
        }
    }

    override fun getAppName(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[DataStoreKeys.APP_NAME_KEY] ?: context.getString(R.string.app_name)
        }
    }

    override suspend fun saveIpAddress(ipAddress: String) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.APP_IP_ADDRESS_KEY] = ipAddress
        }
    }

    override fun getIpAddress(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[DataStoreKeys.APP_IP_ADDRESS_KEY] ?: utils.address
        }
    }

    override suspend fun savePortNumber(port: String) {
        context.dataStore.edit { preference ->
            preference[DataStoreKeys.APP_PORT_KEY] = port
        }
    }

    override fun getPortNmber(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[DataStoreKeys.APP_PORT_KEY] ?: utils.port
        }
    }


    override suspend fun saveShowCase(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.SHOW_CASE_KEY] = value
        }
    }

    override fun getShowCase(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[DataStoreKeys.SHOW_CASE_KEY] ?: false
        }
    }
}
