package com.example.core_ui.admin.presentation.dynamicScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_ui.di.IoDispatcher
import com.example.core_ui.dynamicscreen.presentation.DynamicScreenState
import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.domain.use_cases.action_button.AddActionButtonUseCase
import com.example.database.domain.use_cases.action_button.AllActionButtonByNavigationIdUseCase
import com.example.database.domain.use_cases.action_button.DeleteActionButtonUseCase
import com.example.database.domain.use_cases.slider.AddSliderUseCase
import com.example.database.domain.use_cases.slider.AllSlidersByNavigationItem
import com.example.database.domain.use_cases.slider.DeleteSliderUseCase
import com.example.database.domain.use_cases.slider_button.AddSliderButtonUseCase
import com.example.database.domain.use_cases.slider_button.DeleteSliderButtonUseCase
import com.example.database.domain.use_cases.slider_button.GetSliderButtonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.commonresources.R
import com.example.core_ui.di.MainDispatcher
import com.example.database.data.model.ActionButtons
import com.example.database.data.model.SliderButton
import com.example.database.data.model.Sliders

@HiltViewModel
class AdminDynamicScreenViewModel
    @Inject constructor(
        private val addActionButtonUseCase: AddActionButtonUseCase,
        private val deleteActionButtonUseCase: DeleteActionButtonUseCase,
        private val allActionButtonsUseCase: AllActionButtonByNavigationIdUseCase,
        private val allSlidersByNavigationItem: AllSlidersByNavigationItem,
        private val addSliderUseCase: AddSliderUseCase,
        private val deleteSliderUseCase: DeleteSliderUseCase,
        private val addSliderButtonUseCase: AddSliderButtonUseCase,
        private  val deleteSliderButtonUseCase: DeleteSliderButtonUseCase,
        private val getSliderButtonUseCase: GetSliderButtonUseCase,
        @ApplicationContext private val  context: Context,
        @IoDispatcher val ioDispatcher: CoroutineDispatcher,
        @MainDispatcher val mainDispatcher: CoroutineDispatcher,

        ) :ViewModel(){


    private var job: Job? = null
    private var sliderJob:Job? = null


    var brightness by mutableFloatStateOf(100f)

    private val _navigationItemWithButtons = MutableStateFlow(DynamicScreenState(true))
    val navigationItemWithButtons: StateFlow<DynamicScreenState> = _navigationItemWithButtons

    private var _showDialog = mutableStateOf(false)
    val showDialog: MutableState<Boolean> = _showDialog

    private var _showDeleteDialog = mutableStateOf(false)
    val showDeleteDialog: MutableState<Boolean> = _showDeleteDialog

    private var _showSliderDialog = mutableStateOf(false)
    val showSliderDialog: MutableState<Boolean> = _showSliderDialog

    private var _showSliderButtonDialog = mutableStateOf(false)
    val showSliderButtonDialog: MutableState<Boolean> = _showSliderButtonDialog

    private var _deleteSliderButtonDialog = mutableStateOf(false)
    val deleteSliderButtonDialog: MutableState<Boolean> = _deleteSliderButtonDialog

     var showShutDowndialog by mutableStateOf(false)

    val buttonText = mutableStateOf("")

    val sliderText = mutableStateOf("")

    val sliderButtonText = mutableStateOf("")

    // State to hold the clicked status of buttons
    private val _buttonStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val buttonStates: StateFlow<Map<String, Boolean>> = _buttonStates


    fun setNavigationItem(navigationItem: NavigationItemWithActionButtons) {
        getActionButtons(navigationItem.navigationItem!!.navigationItemId)
    }

    fun updateBrightness(value:Float){
        brightness = value
    }

    fun dialogState(value : Boolean){
        showDialog.value =  value
    }
    fun sliderDialog(value: Boolean){
        _showSliderDialog.value = value
    }

    fun deleteDialogState(value : Boolean){
        showDeleteDialog.value =  value
    }

    fun sliderButtonDialog(value: Boolean){
        _showSliderButtonDialog.value = value
    }

    fun sliderButtonDeleteDialog(value: Boolean){
        _deleteSliderButtonDialog.value = value
    }

    fun toggleButtonState(action: String , isChecked: Boolean) {
//        _buttonStates.value = _buttonStates.value.mapValues { false }
        _buttonStates.value += (action to isChecked)
    }

    fun resetAllButtonStates() {
        _buttonStates.value = _buttonStates.value.mapValues { false }
    }
    fun onEvent(event  : ScreenEvent){

        when(event){
            is ScreenEvent.onAddSliderButtonEvent ->{

                if(sliderButtonText.equals("")){
                    Toast.makeText(context,
                        context.getString(R.string.enter_button_name)
                        ,Toast.LENGTH_SHORT).show()
                }else{
                    val sliderButton = SliderButton(
                        sliderButtonId = 0,
                        navigationItemId =  event.navigationItemId,
                        buttonlabel = sliderButtonText.value,
                        isAdmin = true,
                        action = "${event.navigationItemId}" + "/"+ sliderButtonText.value,
                        isFavorite =  false,
                        position = 0
                    )
                    addSliderButton(sliderButton)
                }
            }
            is ScreenEvent.onDeleteSliderButtonEvent ->{
                deleteSliderButton(event.sliderButton)
            }
            is ScreenEvent.onAddEvent ->{

//                val actionButton = ActionButtons(
//                    actionButtonId = 0,
//                    buttonlabel = buttonText.value,
//                    navigationItemId = event.navigationItemId,
//                    isAdmin = true,
//                    action = "${event.navigationItemId}" + "/"+"${buttonText.value}",
//                    )
//                addActionButton(actionButton)
            }
            is ScreenEvent.onDeleteEvent ->{
                deleteActionButton(event.actionButtons)
            }

            is ScreenEvent.onGetButtonsEvent ->{
                getActionButtons(event.actionButtons.navigationItemId)
            }

            is ScreenEvent.onAddSliderEvent ->{
                if(sliderText.equals("")){
                    Toast.makeText(context,context.getString(R.string.enter_slider_name), Toast.LENGTH_SHORT).show()
                }
                else{
                    val slider = Sliders(
                        sliderId = 0,
                        sliderLabel = sliderText.value,
                        navigationItemId = event.navigationItemId,
                        action = "${event.navigationItemId}" + "/"+"${sliderText.value}",
                        isAdmin = true
                    )
                    addSlider(slider)
                }
            }
            is ScreenEvent.onDeleteSliderEvent ->{
                deleteSlider(event.slider)
            }

            else -> {}
        }
    }


    private fun addSliderButton(sliderButton: SliderButton){
        job?.cancel()
        job = viewModelScope.launch(ioDispatcher) {
            addSliderButtonUseCase(sliderButton)
        }
    }

    private fun deleteSliderButton(sliderButton: SliderButton) {
        job?.cancel()
        job = viewModelScope.launch (ioDispatcher){
            deleteSliderButtonUseCase(sliderButton)
        }
    }

    private fun deleteSlider(
        slider: Sliders
    ){
        sliderJob?.cancel()

        sliderJob = viewModelScope.launch(Dispatchers.IO) {
            deleteSliderUseCase(slider)
            getSlider(slider.navigationItemId)
        }
    }

    fun getSliderButton(navigationItemId: Long){
        job = viewModelScope.launch(ioDispatcher) {
            getSliderButtonUseCase(navigationItemId)
                .onEach {
//                    Log.d("SliderButton"," SliderButton  = ${it.size}")
                    _navigationItemWithButtons.value = _navigationItemWithButtons.value.copy(
                        navigationItemSliderButton = it
                    )
//                    Log.d("SliderButton","${_navigationItemWithButtons.value.navigationItemSliderButton.size}")
                }.launchIn(viewModelScope)
        }
    }
    fun getSlider(navigationItemId: Long) {

        sliderJob?.cancel()
        sliderJob = viewModelScope.launch(ioDispatcher) {
            val sliders = allSlidersByNavigationItem(navigationItemId)
            _navigationItemWithButtons.value = _navigationItemWithButtons.value.copy(
                navigationItemSlider = sliders, // Assuming it's a single slider object, adjust if needed
                isLoading = false
            )
        }
    }

    private fun addSlider(slider: Sliders){
        sliderJob?.cancel()
        sliderJob = viewModelScope.launch(ioDispatcher) {
            addSliderUseCase(slider)
            getSlider(slider.navigationItemId)
        }
    }

    private fun getActionButtons(navigationItemId: Long){
        job?.cancel()
        job = viewModelScope.launch(ioDispatcher) {
            allActionButtonsUseCase(navigationItemId)
                .onEach { data ->
                    _navigationItemWithButtons.value  = _navigationItemWithButtons.value.copy(
                        isLoading = false,
                        navigationItemActionButtons = data
                    )
                }.launchIn(viewModelScope)
        }
    }
    private  fun addActionButton(
        actionButton: ActionButtons
    ){
        job?.cancel()
        job = viewModelScope.launch(ioDispatcher) {
            addActionButtonUseCase(actionButton)
        }
    }

    private fun deleteActionButton(
        actionButton: ActionButtons
    ){
        job?.cancel()

        job = viewModelScope.launch(ioDispatcher) {

            deleteActionButtonUseCase(actionButton)
            getActionButtons(actionButton.navigationItemId)
        }
    }
}