package com.example.mqtt.domain.usecase


import com.example.mqtt.domain.repository.MqttRepository
import javax.inject.Inject

class ConnectMQTTbrokerUseCase
    @Inject constructor(
        private val  mqttRepository: MqttRepository
    ){
        suspend operator fun invoke(onDisconnect: (Boolean) -> Unit , onConnect: (Boolean) -> Unit ){
//            withContext(Dispatchers.IO){
                mqttRepository.connect(onDisconnect , onConnect)
//            }
     }
    }