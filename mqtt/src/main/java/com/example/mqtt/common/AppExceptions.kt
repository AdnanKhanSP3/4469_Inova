package com.example.mqtt.common


// Custom exceptions for Wi-Fi status
class WifiNotEnabledException : Exception("Wi-Fi is not enabled.")
class WifiNotConnectedException(ssid: String) : Exception("Not connected to the Wi-Fi network: $ssid")
class MQTTConnectionException(message: String) : Exception(message)
