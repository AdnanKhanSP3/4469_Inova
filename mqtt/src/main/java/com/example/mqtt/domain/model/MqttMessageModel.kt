package com.example.mqtt.domain.model


data class MqttMessageModel(
    val topic: String,
    val message: String
)