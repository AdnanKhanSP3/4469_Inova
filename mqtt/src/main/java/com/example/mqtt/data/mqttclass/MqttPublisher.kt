package com.example.mqtt.data.mqttclass

import android.util.Log
import com.example.mqtt.di.IoDispatcher
import com.example.mqtt.common.MQTTConnectionException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.Throws


class MqttPublisher
@Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val mqttClient: MqttClient
){

    @Throws(MQTTConnectionException::class , CancellationException::class)
    suspend fun publish(
         topic: String,
         message: String
    ){
        withContext(ioDispatcher){

            try {
                val mqttMessage = MqttMessage(message.toByteArray())
//                mqttClient.publish(topic, message.toByteArray() , 0,false)
                mqttClient.publish(topic, mqttMessage)
            }
            catch (cancel: CancellationException){
                throw cancel
            }
            catch (e: MqttException){
                Log.d("MQTT", e.message.toString())
                throw MQTTConnectionException(e.message.toString())
            }
        }
    }
}