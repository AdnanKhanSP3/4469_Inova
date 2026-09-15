package com.example.mqtt.di

import android.content.Context
import com.example.mqtt.common.utils
import com.example.mqtt.data.mqttclass.MqttClientManager
import com.example.mqtt.data.mqttclass.MqttPublisher
import com.example.mqtt.data.repository.MqttRepositoryImpl
import com.example.mqtt.domain.repository.MqttRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MQTTModule {

    @Singleton
    @ClientID
    @Provides
    fun provideClientID(): String =  utils.MQTTHostName
//        MqttClient.generateClientId()

    @Singleton
    @ServerURI
    @Provides
    fun provideServerURI(): String = utils.mqttServerUri


    @Singleton
    @Provides
    fun providesMQTTClient(
        @ApplicationContext context: Context, @ClientID clientID: String,
                           @ServerURI serverURI: String): MqttClient {
        return MqttClient(serverURI , clientID , MemoryPersistence() )
    }

    @Singleton
    @Provides
    fun providesMQTTConnectOptions(): MqttConnectOptions{
        return MqttConnectOptions().apply {
            isAutomaticReconnect = false
            isCleanSession = false
            connectionTimeout = 5
            keepAliveInterval = 60
        }
    }

    @Provides
    fun provideMQTTRepository(mqttPublisher: MqttPublisher,
                              mqttClientManager: MqttClientManager
    ): MqttRepository {
        return MqttRepositoryImpl(mqttPublisher,mqttClientManager)
    }
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ClientID

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ServerURI