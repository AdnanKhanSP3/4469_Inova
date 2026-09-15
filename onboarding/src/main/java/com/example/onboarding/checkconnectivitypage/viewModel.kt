package com.example.onboarding.checkconnectivitypage

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.mqtt.common.MQTTConnectionException
import com.example.mqtt.domain.usecase.ConnectMQTTbrokerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import javax.inject.Inject


@HiltViewModel
class ConnectivityViewModel
@Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val connectMQTTbrokerUseCase : ConnectMQTTbrokerUseCase,
    ):ViewModel() {

    var task1Checked = mutableStateOf(false)
        private set

    var showModelWifiDialog = mutableStateOf(false)
        private set

    var showMQTTDialog = mutableStateOf(false)


    val showEnableWifiDialog =
        savedStateHandle.getStateFlow("showEnableWifiDialog", false)

    private val _task2Checked = MutableStateFlow(false)
    val task2Checked: StateFlow<Boolean> = _task2Checked

    private val _task3Checked = MutableStateFlow(false)
    val task3Checked: StateFlow<Boolean> = _task3Checked

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    private val _showPermanentSnackbar = MutableStateFlow(false)
    val showPermanentSnackbar: StateFlow<Boolean> = _showPermanentSnackbar

    private var mqttJob: Job? = null

    init {
//        checkMQTT()
    }
    @WorkerThread
    fun checkMQTT(){

        mqttJob?.cancel()

        mqttJob = viewModelScope.launch(Dispatchers.IO){


            try {
                connectMQTTbrokerUseCase.invoke(
                    onDisconnect = {
                        showMQTTDialog.value = true
                    },
                    onConnect = {
                        showMQTTDialog.value = false
                        updateTask2Checked(true)
                        hideSnackbar()
                        initializeDatabase()
                    }
                )

//                Log.d("MQTT","try end  dialog value = ${ showMQTTDialog.value}")

            }catch (e : MQTTConnectionException){
                showMQTTDialog.value = true
            } catch (e: CancellationException){
                showMQTTDialog.value = true
            } catch (e: Exception) {
                showMQTTDialog.value = true
            }
        }
    }

    fun showSnackbarMessage(message: String) {
        _snackbarMessage.value = message
        _showPermanentSnackbar.value = true
    }

     fun hideSnackbar() {
        _showPermanentSnackbar.value = false
    }

    fun showEnableWifiDialogState(value: Boolean) {
        savedStateHandle["showEnableWifiDialog"] = value
    }

    fun showModelWifiDialogState(value: Boolean){
        showModelWifiDialog.value = value
    }

    // Functions to update each task's checked state
    @VisibleForTesting
     fun updateTask1Checked(isChecked: Boolean) {
        task1Checked.value = isChecked
    }

    private fun updateTask2Checked(isChecked: Boolean) {
        _task2Checked.value = isChecked
    }

    private fun updateTask3Checked(isChecked: Boolean) {
        _task3Checked.value = isChecked
    }

    fun isLocationEnabled(context: Context): Boolean {
//        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
//        val isDeviceIdleMode = powerManager.isDeviceIdleMode
//
//        return  locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return true
    }
    @WorkerThread
    private fun initializeDatabase(){
        viewModelScope.launch {
            try {
                delay(2000)
                updateTask3Checked(true)
            }catch (e : Exception){
                updateTask3Checked(false)
            }
        }
    }
}