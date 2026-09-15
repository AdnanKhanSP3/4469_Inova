package com.example.mqtt.domain.usecase

import com.example.mqtt.domain.model.MqttMessageModel
import com.example.mqtt.domain.repository.MqttRepository
import javax.inject.Inject

class PublishMQTTMessageUseCase
@Inject constructor(
    private val mqttRepository: MqttRepository
){

    suspend operator fun invoke(topic: String, message: String){

        val mqttMessage = MqttMessageModel(topic, message)
        mqttRepository.publish(mqttMessage)
    }
}