package com.example.mqtt.domain.repository

import com.example.mqtt.domain.model.MqttMessageModel

interface MqttRepository {
    suspend fun connect(onDisconnect: (Boolean) -> Unit , onConnect: (Boolean) -> Unit)
    suspend fun publish(mqttMessage: MqttMessageModel)
    fun isConnected(): Boolean
    suspend fun subscribe(topic: String, onMessageReceived: (String) -> Unit)
}