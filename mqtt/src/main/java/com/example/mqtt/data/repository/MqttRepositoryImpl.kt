package com.example.mqtt.data.repository

import com.example.mqtt.data.mqttclass.MqttPublisher
import com.example.mqtt.domain.model.MqttMessageModel
import com.example.mqtt.domain.repository.MqttRepository
import com.example.mqtt.data.mqttclass.MqttClientManager
import javax.inject.Inject

class MqttRepositoryImpl
    @Inject constructor(
        private val mqttPublisher: MqttPublisher,
        private val mqttClientManager: MqttClientManager
    ): MqttRepository {

    override suspend fun connect(onDisconnect: (Boolean) -> Unit , onConnect: (Boolean) -> Unit) {
        return mqttClientManager.connectMQTTBroker(onDisconnect , onConnect)
    }

    override  suspend fun publish(mqttMessage: MqttMessageModel) {
         mqttPublisher.publish(mqttMessage.topic, mqttMessage.message)
    }

    override fun isConnected(): Boolean {
        return false
    }

    override suspend fun subscribe(topic: String, onMessageReceived: (String) -> Unit) {
        mqttClientManager.subscribe(topic, onMessageReceived)
    }
}