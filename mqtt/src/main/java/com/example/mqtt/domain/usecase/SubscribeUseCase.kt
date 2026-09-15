package com.example.mqtt.domain.usecase

import com.example.mqtt.domain.repository.MqttRepository
import javax.inject.Inject

class SubscribeUseCase
@Inject constructor(
    private val mqttRepository: MqttRepository
){
    suspend operator fun invoke(topic: String,  onMessageReceived: (String) -> Unit) {
         mqttRepository.subscribe(topic, onMessageReceived)
    }
}