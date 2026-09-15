package com.example.core_ui.admin.presentation

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commonresources.R
//import com.example.core_ui.admin.presentation.component.utils
import com.example.core_ui.di.IoDispatcher
import com.example.core_ui.main.presentation.MainScreenState
import com.example.core_ui.preferenceDataStore.getAppName
import com.example.core_ui.preferenceDataStore.getIpAddress
import com.example.core_ui.preferenceDataStore.getPortNumber
import com.example.core_ui.preferenceDataStore.saveAppName
import com.example.core_ui.preferenceDataStore.saveIpAddress
import com.example.core_ui.preferenceDataStore.savePortNumber
import com.example.database.data.model.NavigationItem
import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.domain.use_cases.navigation_item.AddNavigationItemUseCase
import com.example.database.domain.use_cases.navigation_item.DeleteNavigationItemUseCase
import com.example.database.domain.use_cases.navigation_item.NavigationItemsWithActionButtonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.mqtt.common.utils


@HiltViewModel
class AdminMainScreenViewModel
    @Inject constructor(
        private val addNavigationItemUseCase: AddNavigationItemUseCase,
        private val deleteNavigationItemUseCase: DeleteNavigationItemUseCase,
        private val navigationItemsWithActionButtonsUseCase: NavigationItemsWithActionButtonsUseCase,
        @ApplicationContext private val  context: Context,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
//        private val mqttClientManager: MqttClientManager

    ):ViewModel() {

    private val _navigationItemList = MutableStateFlow(MainScreenState(true))
    val navigationItemList : StateFlow<MainScreenState> = _navigationItemList.asStateFlow()

    private var job: Job? = null

    private var _showDialog = mutableStateOf(false)
    val showDialog: MutableState<Boolean> = _showDialog

    private var _showDeleteDialog = mutableStateOf(false)
    val showDeleteDialog: MutableState<Boolean> = _showDeleteDialog

    private var _showDeleteItemDialog = mutableStateOf(false)
    val showDeleteItemDialog: MutableState<Boolean> = _showDeleteItemDialog

    val itemText = mutableStateOf("")

    private var _contactDialog = mutableStateOf(false)
    val contactDialog: MutableState<Boolean> = _contactDialog

    private var _settingDialog = mutableStateOf(false)
    val settingDialog : MutableState<Boolean> = _settingDialog

    // MutableStateFlow to hold the current app name
    private val _appName = mutableStateOf(context.getString(R.string.app_name))
    val appName: MutableState<String> = _appName

//    private val _ipAddress = mutableStateOf(utils.address)
    private val _ipAddress = mutableStateOf(utils.mqttServerUri)
    val ipAddress: MutableState<String> = _ipAddress

    private val _port = mutableStateOf(utils.port)
    val port: MutableState<String> = _port

    var selectedItem = mutableStateOf<NavigationItemWithActionButtons?>(null)

    init {
        loadAppName()
        loadIpAddress()
        loadPort()
        getNavigationItemsWithActionButtons()
    }

    fun dialogState(value : Boolean){
        showDialog.value =  value
    }

    fun deleteDialogState(value : Boolean){
        showDeleteDialog.value =  value
    }

    fun deleteDialogItemState(value : Boolean){
        showDeleteItemDialog.value =  value
    }

    fun contactDialogState(value: Boolean){
        _contactDialog.value = value
    }


    fun settingDialogState(value: Boolean){
        settingDialog.value = value
    }

    private fun loadIpAddress() {
        viewModelScope.launch {
            val addr = withContext(ioDispatcher){
                getIpAddress(context).first()
            }
            _ipAddress.value = addr
        }
    }

    // Function to load the app name from DataStore
    // Function to load the app name from DataStore
    private fun loadAppName() {
        viewModelScope.launch {
            val name = withContext(ioDispatcher) {
                getAppName(context).first()
            }
            _appName.value = name
        }
    }

   private fun loadPort(){
        viewModelScope.launch {
            val port = withContext(ioDispatcher){
                getPortNumber(context).first()
            }
            _port.value = port
        }
    }

    fun updatePortNumber(newPort: String){
        viewModelScope.launch(ioDispatcher) {
            savePortNumber(context,newPort)
            _port.value = newPort
        }
    }
    // Function to update and save the app name to DataStore
    fun updateAppName(newAppName: String) {
        viewModelScope.launch(ioDispatcher) {
            // Save the new app name in DataStore
            saveAppName(context, newAppName)
            // Update the state flow with the new value
            _appName.value = newAppName
        }
    }

    fun publishMessage(message: String) {
        viewModelScope.launch {
//            mqttClientManager.publish(message)
        }
    }
    // Function to update and save the app name to DataStore
    fun updateIpAddress(newIpAddress: String) {
        viewModelScope.launch(ioDispatcher) {
            // Save the new app name in DataStore
            saveIpAddress(context, newIpAddress)
            // Update the state flow with the new value
            _ipAddress.value = newIpAddress
        }
    }

     fun addNavigationItem(label: String){

//        job?.cancel()
//        job = viewModelScope.launch(ioDispatcher) {
//            val item = NavigationItem(
//                navigationItemId = 0,
//                label = label,
//                route = "",
//                createdByAdmin = true
//            )
//            addNavigationItemUseCase(item)
//            getNavigationItemsWithActionButtons()
//        }
    }

    fun deleteNavigationItem(navigationItem: NavigationItem){

        job?.cancel()

        job = viewModelScope.launch(ioDispatcher) {
            deleteNavigationItemUseCase(navigationItem)
            getNavigationItemsWithActionButtons()
        }
    }

    fun getNavigationItemsWithActionButtons() {
        job?.cancel()
        job = viewModelScope.launch {
            val items = withContext(ioDispatcher) {
                navigationItemsWithActionButtonsUseCase()
            }

            _navigationItemList.update {
                it.copy(
                    isLoading = false,
                    navigationItems = items
                )
            }

            selectedItem.value = items.firstOrNull()
        }
    }

//    fun getNavigationItemsWithActionButtons()
//    {
//        job?.cancel()
//        job = viewModelScope.launch(ioDispatcher) {
//
//            _navigationItemList.update {
//                it.copy(
//                    isLoading = false,
//                    navigationItems = navigationItemsWithActionButtonsUseCase()
//                )
//            }
//            selectedItem.value = _navigationItemList.value.navigationItems.firstOrNull()
//        }
//    }

    override fun onCleared() {
        viewModelScope.cancel()
        job?.cancel()
        super.onCleared()
    }
}