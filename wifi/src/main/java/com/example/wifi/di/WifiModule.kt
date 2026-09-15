package com.example.wifi.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import com.example.wifi.data.ConnectivityDataSource.ConnectivityDataSourceImpl
import com.example.wifi.data.ConnectivityDataSource.IConnectivityDataSource
import com.example.wifi.data.WifiInfoDataSource.IWifiInfoDataSource
import com.example.wifi.data.WifiInfoDataSource.WifiInfoDataSourceImpl
import com.example.wifi.domain.wifiobserver.WifiStatusObserver
import com.example.wifi.domain.wifiobserver.WifiStatusObserverImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object WifiModule {

    @Provides
    fun providesWifiManager(@ApplicationContext context: Context): WifiManager {
        return context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    @Provides
    fun providesConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    @Provides
    fun providesConnectivityDataSource(connectivityManager: ConnectivityManager): IConnectivityDataSource {
        return ConnectivityDataSourceImpl(connectivityManager)
    }

    @Provides
    fun  providesWifiInfoDataSource(wifiManager: WifiManager): IWifiInfoDataSource {
        return WifiInfoDataSourceImpl(wifiManager)
    }

    @Provides
    fun providesWifiStatusObserver(
        connectivity: IConnectivityDataSource,
        wifiInfo: IWifiInfoDataSource
    ): WifiStatusObserver {
        return WifiStatusObserverImpl(connectivity, wifiInfo)
    }

}