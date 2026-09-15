package com.example.core_ui.main.presentation

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.core_ui.di.IoDispatcher
import com.example.core_ui.main.component.AppMode
import com.example.core_ui.preferenceDataStore.domain.use_cases.GetShowCaseUseCase
import com.example.core_ui.preferenceDataStore.domain.use_cases.SaveShowCaseUseCase
import com.example.database.domain.use_cases.navigation_item.DeleteNavigationItemUseCase
import com.example.database.domain.use_cases.navigation_item.NavigationItemsWithActionButtonsUseCase
import com.example.database.domain.use_cases.navigation_item.UpdateNavigationItemUseCase
import com.example.mqtt.common.MQTTConnectionException
import com.example.mqtt.domain.usecase.ConnectMQTTbrokerUseCase
import com.example.mqtt.domain.usecase.PublishMQTTMessageUseCase
import com.example.mqtt.domain.usecase.SubscribeUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val deleteNavigationItemUseCase: DeleteNavigationItemUseCase,
        private val navigationItemsWithActionButtonsUseCase: NavigationItemsWithActionButtonsUseCase,
        private val updateNavigationItemUseCase: UpdateNavigationItemUseCase,
        private val connectMQTTbrokerUseCase : ConnectMQTTbrokerUseCase,
        private val subscribedUseCase: SubscribeUseCase,
        private val publishUseCase: PublishMQTTMessageUseCase,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
        private val saveShowCaseUseCase: SaveShowCaseUseCase,
        private val getShowCase: GetShowCaseUseCase
        ): ViewModel() {

    private val _currentMode = MutableStateFlow(AppMode.USER)
    val currentMode: StateFlow<AppMode> = _currentMode

    var enterPassword by mutableStateOf("")
        private set

    private val _testItemList = MutableStateFlow(MainScreenState(true))

    val itemText = mutableStateOf("")

    private val _showCaseState = MutableStateFlow(false)
    val showCaseState: StateFlow<Boolean> = _showCaseState

    private var _showDialog = mutableStateOf(false)
    val showDialog: MutableState<Boolean> = _showDialog

    private var _showAddItemDialog = mutableStateOf(false)
    val showAddItemDialog: MutableState<Boolean> = _showAddItemDialog

    val testItemList: StateFlow<MainScreenState> = _testItemList.asStateFlow()

    private var _showDeleteItemDialog = mutableStateOf(false)
    val showDeleteItemDialog: MutableState<Boolean> = _showDeleteItemDialog

    private var _contactDialog = mutableStateOf(false)
    val contactDialog: MutableState<Boolean> = _contactDialog

    private val _snackbarMessage = MutableStateFlow("")
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    private val _showPermanentSnackbar = MutableStateFlow(false)
    val showPermanentSnackbar: StateFlow<Boolean> = _showPermanentSnackbar

    private var job: Job? = null

    private var mqttJob: Job? = null

    var showEnableWifiDialog = mutableStateOf(false)
        private set

    var showModelWifiDialog = mutableStateOf(false)
        private set

    var showMQTTDialog = mutableStateOf(false)

    var demoSwitch by mutableStateOf(false)
    var demoAction by mutableStateOf(false)

    var notificationDialog by mutableStateOf(false)
    var notificationDialogVisible by mutableStateOf(false)

//    var syncNotificationDialog by mutableStateOf(false)

    init {
        getNavigationItemsWithActionButtons()
        getshowCaseState()
    }

    fun getshowCaseState(){
        viewModelScope.launch (ioDispatcher){
            getShowCase.invoke().collect{
                _showCaseState.value = it
            }
        }
    }

    fun updateShowCaseState(value: Boolean){
        viewModelScope.launch(ioDispatcher) {
            saveShowCaseUseCase(value)
        }
    }

    fun hideSnackbar() {
        _showPermanentSnackbar.value = false
    }

    fun showSnackbarMessage(message: String) {
        _snackbarMessage.value = message
        _showPermanentSnackbar.value = true
    }

    @WorkerThread
    fun checkMQTT(){

        mqttJob = viewModelScope.launch(ioDispatcher) {
            try {

                connectMQTTbrokerUseCase.invoke(
                    onDisconnect = {
                        showMQTTDialog.value = true
                    },
                    onConnect = {
                        showMQTTDialog.value = false

                        //hide notification view also
                        notificationDialogVisible = true

                        notificationDialog = false
                        _showPermanentSnackbar.value = false
                    }
                )

            }catch (e : MQTTConnectionException){
                showMQTTDialog.value = true
            } catch (e: CancellationException){
                showMQTTDialog.value = true
            } catch (e: Exception) {
                showMQTTDialog.value = true
            }
        }
    }

    fun publishMessage(topic: String, message: String){

        viewModelScope.launch(ioDispatcher) {
            try {
                publishUseCase(topic, message)
            }
            catch (e : MQTTConnectionException){
                Log.d("MQTT", "Publish failed: topic=$topic", e)
            } catch (e: CancellationException){

            } catch (e: Exception) {

            }
        }
    }

    fun subscribeTopic(topic: String, onMessageReceived: (String) -> Unit){
        viewModelScope.launch(ioDispatcher) {
        try {
             subscribedUseCase(topic, onMessageReceived)
            }
            catch (e : MQTTConnectionException){
//                showMQTTDialog.value = true
            } catch (e: CancellationException){
//                showMQTTDialog.value = true
            } catch (e: Exception) {
//                showMQTTDialog.value = true
            }
        }
    }

    fun subscribeMQTTSlider(topic: String , updateState: (String) -> Unit){

        viewModelScope.launch(ioDispatcher) {

            try {
                subscribedUseCase.invoke(topic){message ->
                    updateState(message)
                }
            } catch (e : MQTTConnectionException){
                showMQTTDialog.value = true
            } catch (e: CancellationException){
                showMQTTDialog.value = true
            } catch (e: Exception) {
                showMQTTDialog.value = true
            }
        }
    }

    fun showEnableWifiDialogState(value: Boolean) {

        showEnableWifiDialog.value = value
    }

    fun showModelWifiDialogState(value: Boolean){
        showModelWifiDialog.value = value
    }

    // Switch between modes and set the current mode
    fun switchMode(mode: AppMode) {
        _currentMode.value = mode
    }

    fun updatePassword(value: String) {
        enterPassword = value
    }

    fun dialogState(value: Boolean) {
        showDialog.value = value
        updatePassword("")
    }

    fun dialogAddItemState(value: Boolean) {
        showAddItemDialog.value = value
    }

    fun deleteDialogItemState(value : Boolean){
        showDeleteItemDialog.value =  value
    }

    fun contactDialogState(value: Boolean){
        _contactDialog.value = value
    }

    fun getNavigationItemsWithActionButtons() {

        job?.cancel()

        job = viewModelScope.launch {

            withContext(ioDispatcher){

                _testItemList.update {
                    it.copy(
                        isLoading = false,
                        navigationItems = navigationItemsWithActionButtonsUseCase()
                    )
                }
            }
        }

    }

    fun saveNavigationOrder() {

        viewModelScope.launch(ioDispatcher) {

            _testItemList.value.navigationItems
                .forEachIndexed { index, item ->

                    item.navigationItem?.let { navItem ->

                        updateNavigationItemUseCase(
                            navItem.copy(
                                position = index
                            )
                        )
                    }
                }
        }
    }

    fun moveNavigationItem(
        fromIndex: Int,
        toIndex: Int
    ) {

        val current =
            _testItemList.value.navigationItems.toMutableList()

        val item = current.removeAt(fromIndex)

        current.add(toIndex, item)

        _testItemList.update {
            it.copy(
                navigationItems = current
            )
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
        job?.cancel()
        super.onCleared()
    }
}