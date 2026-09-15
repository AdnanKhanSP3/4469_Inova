package com.example.wifi.presentation


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wifi.presentation.component.PermissionUiEvent
import com.example.wifi.presentation.component.PermissionUiState
import com.example.wifi.presentation.wifi.WifiManagerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.Q)
@HiltViewModel
class PermissionViewModel
    @Inject constructor(
    private val wifiManagerHelper: WifiManagerHelper,
) : ViewModel() {

    private val _state = MutableStateFlow(PermissionUiState())
    val state: StateFlow<PermissionUiState> = _state.asStateFlow()

    private val _events = Channel<PermissionUiEvent>(Channel.BUFFERED)
    val events: Flow<PermissionUiEvent> = _events.receiveAsFlow()


    var _showSettingsDialog by mutableStateOf(false)

    /** Call once when the screen opens (UI passes current permission status). */
    fun onScreenShown(currentlyGranted: Boolean) {
        _state.update { it.copy(isGranted = currentlyGranted) }

        if (!currentlyGranted) {
            viewModelScope.launch { _events.send(PermissionUiEvent.RequestPermission) }
        }
    }

    init {
        checkWifi("4412 CLA Grill #1")
    }
    fun checkWifi(targetSSID :  String){
        try {

            val ok = wifiManagerHelper.isConnectedToSpecifiedWifi(targetSSID)

            if(!ok){
//                val check = wifiManagerHelper.suggestNetworkAutoJoin(targetSSID,"SP3intern#2015")
            }
            else
            {
                Log.d("TAG", "checkWifi: $ok")
            }

        }catch (e: Exception){
            Log.d("TAG", e.message.toString())
        }
    }

    /** UI reports back the result of the permission request. */
    fun onPermissionResult(granted: Boolean, shouldShowRationale: Boolean) {
        _state.update {
            it.copy(
                isGranted = granted,
                showRationaleDialog = !granted && shouldShowRationale
            )
        }

        // If denied AND no rationale -> user likely selected "Don't ask again"
        if (!granted && !shouldShowRationale) {
            viewModelScope.launch { _events.send(PermissionUiEvent.OpenAppSettings) }
        }
    }

    fun onRationaleDismiss() {
        _state.update { it.copy(showRationaleDialog = false) }
    }

    fun onRationaleConfirmRequestAgain() {
        viewModelScope.launch { _events.send(PermissionUiEvent.RequestPermission) }
    }

}