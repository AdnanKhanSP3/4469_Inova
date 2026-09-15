package com.example.core_ui.setting

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_ui.component.SliderType
import com.example.core_ui.di.IoDispatcher
import com.example.core_ui.di.MainDispatcher
import com.example.mqtt.common.MQTTConnectionException
import com.example.mqtt.domain.usecase.SubscribeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class SettingViewModel
@Inject constructor(
    private val subscribedUseCase: SubscribeUseCase,
    @ApplicationContext private val  context: Context,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher val mainDispatcher: CoroutineDispatcher,
)
    : ViewModel(){


    init {
//        subscribeToSliderTopics()
    }
        var turgriffSlider by mutableStateOf(0f)

    var blinkerSlider by mutableStateOf(0f)

    var lichtleisteSlider by mutableStateOf(0f)
    var panelSlider by mutableStateOf(0f)
    var ambientSlider by mutableStateOf(0f)
    var kartentascheSlider by mutableStateOf(0f)
    var lightbarSlider by mutableStateOf(0f)
    var stoplightSlider by mutableStateOf(0f)
    var masterSlider by mutableStateOf(0f)
    var turoffnerOutSlider by mutableStateOf(0f)
    var turoffnerInSlider by mutableStateOf(0f)

    var showShutDowndialog by mutableStateOf(false)

    private var _showSliderBtnValueDialog = mutableStateOf(false)
    val showSliderBtnValueDialog: MutableState<Boolean> = _showSliderBtnValueDialog

    var updatedSliderBtnValue by mutableStateOf(0)

    var selectedSlider by mutableStateOf<SliderType?>(null)
        private set

    fun selectSlider(slider: SliderType) {
        selectedSlider = slider
    }

    //slider btn slider value
    fun sliderBtnValueDialogState(value: Boolean){
        _showSliderBtnValueDialog.value = value
    }

    fun setSelectedSliderValue(value: Float) {
        when (selectedSlider) {
            SliderType.TURGRIFF -> turgriffSlider = value
            SliderType.BLINKER -> blinkerSlider = value
            SliderType.LICHTLEISTE -> lichtleisteSlider = value
            SliderType.PANEL -> panelSlider = value
            SliderType.AMBIENT -> ambientSlider = value
            SliderType.KARTENTASCHE -> kartentascheSlider = value
            SliderType.LIGHTBAR -> lightbarSlider = value
            SliderType.STOPLIGHT -> stoplightSlider = value
            SliderType.MASTER -> masterSlider = value
            null -> Unit
        }
    }


    /*
    private fun subscribeToSliderTopics() {
        subscribeTopic("Settings/Turgriff") {
            Log.d("TAG","received value = $it")
            turgriffSlider = it
        }

        subscribeTopic("Settings/blinker") {
            blinkerSlider = it
        }

        subscribeTopic("Settings/lichtleiste") {
            lichtleisteSlider = it
        }

        subscribeTopic("Settings/panel") {
            panelSlider = it
        }

        subscribeTopic("Settings/Ambiente Turspiegel") {
            ambientSlider = it
        }

        subscribeTopic("Settings/kartentasche") {
            kartentascheSlider = it
        }

        subscribeTopic("Settings/lightbar") {
            lightbarSlider = it
        }

        subscribeTopic("Settings/stoplight") {
            stoplightSlider = it
        }

        subscribeTopic("Settings/master") {
            masterSlider = it
        }
    }

    fun subscribeTopic(
        topic: String,
        updateSlider: (Float) -> Unit
    ){

        viewModelScope.launch(ioDispatcher) {
            try {
                subscribedUseCase(topic){message ->
                    val value = message.toFloatOrNull() ?: return@subscribedUseCase
                    val validValue = value.coerceIn(0f, 100f)

                }
            }
            catch (e: CancellationException){
            }
            catch (e: MQTTConnectionException) {
                Log.e(
                    "MQTT",
                    "Subscription failed: $topic",
                    e
                )
            } catch (e: Exception) {
                Log.e(
                    "MQTT",
                    "Unexpected subscription error: $topic",
                    e
                )
            }
        }
    }

     */
}