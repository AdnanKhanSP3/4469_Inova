package com.example.core_ui.preferenceDataStore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.commonresources.R
import com.example.core_ui.admin.presentation.component.utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the extension function to get DataStore instance
val Context.dataStore by preferencesDataStore(name = "settings")

object DataStoreKeys {
    val APP_NAME_KEY = stringPreferencesKey("app_name")
    val APP_IP_ADDRESS_KEY = stringPreferencesKey("app_ip_address")
    val APP_PORT_KEY = stringPreferencesKey("app_port")
    val SHOW_CASE_KEY = booleanPreferencesKey("show_case")
}

// Repository or utility function to interact with DataStore
suspend fun saveAppName(context: Context, name: String) {
    context.dataStore.edit { preferences ->
        preferences[DataStoreKeys.APP_NAME_KEY] = name
    }
}
fun getAppName(context: Context): Flow<String> {
    return context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.APP_NAME_KEY] ?: context.getString(R.string.app_name)
    }
}
suspend fun saveIpAddress(context: Context, ipAddress: String) {
    context.dataStore.edit { preferneces ->
        preferneces[DataStoreKeys.APP_IP_ADDRESS_KEY] = ipAddress
    }
}
fun getIpAddress(context: Context): Flow<String> {
    return context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.APP_IP_ADDRESS_KEY] ?: utils.address
    }
}

suspend fun savePortNumber(context: Context, port: String) {
    context.dataStore.edit { preferences ->
        preferences[DataStoreKeys.APP_PORT_KEY] = port
    }
}

fun getPortNumber(context: Context):Flow<String>{
    return context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.APP_PORT_KEY] ?: utils.port
    }
}

suspend fun saveShowCase(context: Context, value: Boolean) {
    context.dataStore.edit  { preferences ->
        preferences[DataStoreKeys.SHOW_CASE_KEY] = value
    }
}

fun getShowCase(context: Context):Flow<Boolean>{
    return context.dataStore.data.map { preference ->
        preference[DataStoreKeys.SHOW_CASE_KEY] ?: false
    }
}