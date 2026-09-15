package com.example.mqtt.data.mqttclass

import android.content.Context
import android.util.Log
import com.example.commonresources.R
import com.example.mqtt.common.MQTTConnectionException
import dagger.hilt.android.qualifiers.ApplicationContext
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException


@Singleton
class MqttClientManager
 @Inject constructor(
     @ApplicationContext private val context: Context,
     private val mqttClient: MqttClient,
     private val mqttConnectOptions: MqttConnectOptions,
     ){

     @Throws(
         MQTTConnectionException::class ,
         CancellationException::class,
         Exception::class
     )
     fun connectMQTTBroker(
         onDisconnect: (Boolean) -> Unit,
         onConnect: (Boolean) -> Unit
     ){
         try {

             setupCallback(
                 onDisconnect ,
                 onConnect
             )

             if (!mqttClient.isConnected){
                 mqttClient.connect(mqttConnectOptions)
             }

             }catch (cancel: CancellationException){
                 throw cancel
             }
             catch (e: MqttException){
                 throw MQTTConnectionException(context.getString(R.string.no_connection))

             }catch (ex: Exception){
                 throw ex
             }
     }

     fun subscribe(topic: String, onMessageReceived: (String) -> Unit) {
//         try {

             mqttClient.subscribe(topic ) { _, message ->
                 Log.d("TAG","subscribe message = ${message.toString()}")
                 onMessageReceived(message.toString())
             }
//         }catch (cancel: CancellationException){
//             throw cancel
//         }
//         catch (e: MqttException){
//             throw MQTTConnectionException(context.getString(R.string.no_connection))
//
//         }catch (ex: Exception){
//             throw ex
//         }

    }

    private fun  setupCallback(onDisconnect: (Boolean) -> Unit  , onConnect: (Boolean)->Unit ){
        mqttClient.setCallback(object : MqttCallbackExtended {
            override fun connectionLost(cause: Throwable?) {
                onDisconnect(true)
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                //TODO (Not implemented)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                //TODO (Not implemented)
            }

            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
               onConnect(true)
            }
        })
    }
}