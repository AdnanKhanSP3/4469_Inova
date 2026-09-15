package com.example.onboarding.checkconnectivitypage.component

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager

class WifiBroadcastReceiver (
    private val onWifiStateChanged: () -> Unit
): BroadcastReceiver() {


    override fun onReceive(p0: Context?, intent: Intent?) {

        val action = intent?.action

        if (action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {

            val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)

            if (networkInfo?.type == ConnectivityManager.TYPE_WIFI) {
                if (networkInfo.isConnected) {
                    // Wi-Fi connected
                    this.onWifiStateChanged()
                } else {
                    // Wi-Fi disconnected
                    this.onWifiStateChanged()
                }
            }
        }
    }
}