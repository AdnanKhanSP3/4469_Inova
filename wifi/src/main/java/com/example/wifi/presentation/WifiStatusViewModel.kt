package com.example.wifi.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wifi.domain.model.WifiStatus
import com.example.wifi.domain.wifiobserver.WifiStatusObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WifiStatusViewModel
@Inject constructor(
    private val wifiStatusObserver: WifiStatusObserver
) : ViewModel() {

//    private val targetSsid = MutableStateFlow("4412 CLA Grill #2") // set from UI
    private val targetSsid = MutableStateFlow("SP3") // set from UI

    val wifiStatus: StateFlow<WifiStatus> =
        targetSsid
            .flatMapLatest { ssid -> wifiStatusObserver.observe(ssid) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = WifiStatus(isWifiConnected = false, ssid = null, isTargetSsid = false)
            )

    fun setTargetSsid(ssid: String) {
        targetSsid.value = ssid
    }
}