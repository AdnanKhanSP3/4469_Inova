package com.example.core_ui.dynamicscreen.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.commonresources.R
import com.example.core_ui.di.IoDispatcher
import com.example.core_ui.di.MainDispatcher
import com.example.database.data.model.ActionButtons
import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.data.model.SliderButton
import com.example.database.data.model.Sliders
import com.example.database.domain.use_cases.action_button.AllActionButtonByNavigationIdUseCase
import com.example.database.domain.use_cases.action_button.DeleteActionButtonUseCase
import com.example.database.domain.use_cases.slider.AddSliderUseCase
import com.example.database.domain.use_cases.slider.AllSlidersByNavigationItem
import com.example.database.domain.use_cases.slider.DeleteSliderUseCase
import com.example.database.domain.use_cases.slider_button.DeleteSliderButtonUseCase
import com.example.database.domain.use_cases.slider_button.GetSliderButtonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import android.util.Base64
import com.example.core_ui.preferenceDataStore.getAppName
import com.example.database.domain.use_cases.action_button.AddToFavourite
import com.example.database.domain.use_cases.action_button.AllFavouriteActionButtonUseCase
import com.example.database.domain.use_cases.action_button.DeleteFavourite
import com.example.database.domain.use_cases.action_button.UpdateAllActionButtonsPositionUseCase
import com.example.database.domain.use_cases.action_button.UpdateFavouriteStatusUseCase
import com.example.database.domain.use_cases.action_button.UpdatePositionUseCase
import com.example.database.domain.use_cases.action_button.UpdateActionBtnText
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext


@HiltViewModel
class DynamicScreenViewModel
@Inject constructor(

    private val updateFavouriteStatusUseCase: UpdateFavouriteStatusUseCase,
    private val updateAllSliderButtonsPositionUseCase: UpdateAllActionButtonsPositionUseCase,
    private val allActionButtonsUseCase: AllActionButtonByNavigationIdUseCase,
    private val allSlidersByNavigationItem: AllSlidersByNavigationItem,
    private val deleteSliderButtonUseCase: DeleteSliderButtonUseCase,
    private val getSliderButtonUseCase: GetSliderButtonUseCase,
    private val allFavouriteSliderButtonUseCase: AllFavouriteActionButtonUseCase,
    private val updateSliderBtnText: UpdateActionBtnText,
    private val addToFavourite: AddToFavourite,
    private val deleteFavourite: DeleteFavourite,
    private val updatePositionUseCase:UpdatePositionUseCase,
    @ApplicationContext private val  context: Context,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher val mainDispatcher: CoroutineDispatcher,
    ):ViewModel()
{

    private var job:Job? = null
    private var sliderJob:Job? = null

    var brightness by mutableFloatStateOf(100f)

    var updatedSliderBtnText by mutableStateOf("")

    var updatedSliderBtnValue by mutableStateOf(0)

    var warmWhiteBtn by mutableStateOf(false)
    var coldWhiteBtn by mutableStateOf(true)

    // State to hold the clicked status of buttons
    private val _buttonStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val buttonStates: StateFlow<Map<String, Boolean>> = _buttonStates

    private val _navigationItemWithButtons = MutableStateFlow(DynamicScreenState(true))
    val navigationItemWithButtons: StateFlow<DynamicScreenState> = _navigationItemWithButtons

    private var _showDialog = mutableStateOf(false)
    val showDialog: MutableState<Boolean> = _showDialog

    private var _showSliderDialog = mutableStateOf(false)
    val showSliderDialog: MutableState<Boolean> = _showSliderDialog

    private var _showSliderButtonDialog = mutableStateOf(false)
    val showSliderButtonDialog: MutableState<Boolean> = _showSliderButtonDialog

    private var _deleteSliderButtonDialog = mutableStateOf(false)
    val deleteSliderButtonDialog: MutableState<Boolean> = _deleteSliderButtonDialog

    private var _shutDownDialog = mutableStateOf(false)
    val shutDownDialog: MutableState<Boolean> = _shutDownDialog

    val buttonText = mutableStateOf("")
    val sliderText = mutableStateOf("")
    val sliderButtonText = mutableStateOf("")

    private var _showDeleteDialog = mutableStateOf(false)
    val showDeleteDialog: MutableState<Boolean> = _showDeleteDialog

    private var _showDeleteSliderDialog = mutableStateOf(false)
    val showDeleteSliderDialog: MutableState<Boolean> = _showDeleteSliderDialog

    private var _showUpdateSliderBtnDialog = mutableStateOf(false)
    val showUpdateSliderBtnDialog: MutableState<Boolean> = _showUpdateSliderBtnDialog

    private var _showSliderBtnValueDialog = mutableStateOf(false)
    val showSliderBtnValueDialog: MutableState<Boolean> = _showSliderBtnValueDialog

    // MutableStateFlow to hold the current app name
    private val _appName = mutableStateOf(context.getString(R.string.app_name))
    val appName: MutableState<String> = _appName

    var showShutDowndialog by mutableStateOf(false)

    var insideFavrt by mutableStateOf(false)

    fun aesExample(originalMessage: String) {
        // Generate a random AES key
        val keyGenerator = KeyGenerator.getInstance("AES") //AES (Advance encryption standard)
        keyGenerator.init(256) // 128, 192, or 256
        val secretKey: SecretKey = keyGenerator.generateKey()

        // Encryption
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedBytes = cipher.doFinal(originalMessage.toByteArray(StandardCharsets.UTF_8))
        val encryptedMessage = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

        println("Encrypted message: $encryptedMessage")

        // Decryption
        cipher.init(Cipher.DECRYPT_MODE, secretKey)

        val decryptedBytes = cipher.doFinal(Base64.decode(encryptedMessage, Base64.DEFAULT))
        val decryptedMessage = String(decryptedBytes, StandardCharsets.UTF_8)

        println("Decrypted message: $decryptedMessage")
    }

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

    fun sliderButtonDialog(value: Boolean){
        _showSliderButtonDialog.value = value
    }

    fun sliderButtonDeleteDialog(value: Boolean){
        _deleteSliderButtonDialog.value = value
    }

    init {
        loadAppName()
    }

    fun toggleButtonState(action: String , isChecked: Boolean) {
        _buttonStates.value += (action to isChecked)
    }

    fun resetAllButtonStates() {
        _buttonStates.value = _buttonStates.value.mapValues { false }
    }

    fun deleteDialogState(value : Boolean){
        showDeleteDialog.value =  value
    }

    fun UpdateSliderBtnState(value: Boolean){
        _showUpdateSliderBtnDialog.value = value

    }

    //slider btn slider value
    fun sliderBtnValueDialogState(value: Boolean){
        _showSliderBtnValueDialog.value = value

    }

    fun deleteSliderDialogState(value : Boolean){
        showDeleteSliderDialog.value =  value
    }

    // Function to load the app name from DataStore
    private fun loadAppName() {
        viewModelScope.launch {
            val name = withContext(ioDispatcher) {
                getAppName(context).first()
            }
            _appName.value = name
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

    /*
    fun getSliderButton(navigationItemId: Long){

        job?.cancel()

        job = viewModelScope.launch {

            if(navigationItemId.toInt() == 3 ){
                insideFavrt = true
                allFavouriteSliderButtonUseCase()
                    .collect{ sliderButtons ->
                        _navigationItemWithButtons.update {
                            it.copy(
                                navigationItemSliderButton = sliderButtons
                            )
                        }
                    }
            }
            else
            {
                insideFavrt = false
                getSliderButtonUseCase(navigationItemId)
                    .collect { sliderButtons ->
                        _navigationItemWithButtons.update {
                            it.copy(
                                navigationItemSliderButton = sliderButtons
                            )
                        }
                    }
            }
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

    fun deleteSliderButton(sliderButton: SliderButton) {
        job?.cancel()
        job = viewModelScope.launch (ioDispatcher){
            deleteSliderButtonUseCase(sliderButton)
        }
    }



    fun saveSliderOrder() {


        // 1. Take a snapshot of the current list order from the UI state
        val currentOrderedList = _navigationItemWithButtons.value.navigationItemSliderButton

        viewModelScope.launch(ioDispatcher) {
            // 2. Prepare the items with their new positions in memory
            val updatedPositionsList = currentOrderedList.mapIndexed { index, item ->
                item.copy(position = index)
            }

            // 3. Call your UseCase/DAO with the entire list at once
            // This executes as a single transaction in Room!
            updateAllSliderButtonsPositionUseCase(updatedPositionsList)

        }
    }

     */

    fun moveSliderButton(
        fromIndex: Int,
        toIndex: Int
    ) {

        val current =
            _navigationItemWithButtons.value
                .navigationItemSliderButton
                .toMutableList()

        if (fromIndex !in current.indices) return
        if (toIndex !in current.indices) return


        // 1. Move the item physically in the list
        val item = current.removeAt(fromIndex)

        current.add(
            toIndex,
            item
        )

        _navigationItemWithButtons.value =
            _navigationItemWithButtons.value.copy(
                navigationItemSliderButton = current
            )
    }

    /*
    fun addFavouriteSliderBtn(sliderButton: SliderButton){

       //new favrt function testing
       viewModelScope.launch(ioDispatcher) {
           updateFavouriteStatusUseCase(
               buttonId = sliderButton.sliderButtonId,
               isFav = true
           )

           if (insideFavrt){
               getSliderButton(3L)
           }else
           {
               getSliderButton(sliderButton.navigationItemId)
           }
       }

        /*
         viewModelScope.launch(ioDispatcher) {

             addToFavourite(btn)

             if (insideFavrt){
                 getSliderButton(3L)
             }else
             {
                 getSliderButton(sliderButton.navigationItemId)
             }
        }

         */
    }

    fun deleteFavouriteSliderBtn(sliderButton: SliderButton){


        //new favrt function testing
        viewModelScope.launch(ioDispatcher) {
            updateFavouriteStatusUseCase(
                buttonId = sliderButton.sliderButtonId,
                isFav = false
            )

            if (insideFavrt){
                getSliderButton(3L)
            }else
            {
                getSliderButton(sliderButton.navigationItemId)
            }
        }
        /*
        val btn = sliderButton.copy(
            isFavorite = false
        )

         viewModelScope.launch(ioDispatcher) {

            deleteFavourite(btn)

            if (insideFavrt){
                getSliderButton(3L)
            }else
            {
                getSliderButton(sliderButton.navigationItemId)
            }
        }


         */
    }

    fun updateSliderBtnText(sliderButton: SliderButton){

        if (!updatedSliderBtnText.equals("")){

            job?.cancel()
            val btn = sliderButton.copy(
                buttonlabel = updatedSliderBtnText
            )

            job = viewModelScope.launch(ioDispatcher) {
                addToFavourite(btn)
                getSliderButton(sliderButton.navigationItemId)
            }

        }
        else{
            Toast.makeText(context,
                "Please enter button name"
                , Toast.LENGTH_SHORT).show()
        }
    }

     */
}