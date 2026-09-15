package com.example.core_ui.animation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core_ui.di.IoDispatcher
import com.example.core_ui.di.MainDispatcher
import com.example.core_ui.dynamicscreen.presentation.DynamicScreenState
import com.example.database.data.model.ActionButtons
import com.example.database.domain.use_cases.action_button.AddToFavourite
import com.example.database.domain.use_cases.action_button.AllActionButtonByNavigationIdUseCase
import com.example.database.domain.use_cases.action_button.AllFavouriteActionButtonUseCase
import com.example.database.domain.use_cases.action_button.DeleteFavourite
import com.example.database.domain.use_cases.action_button.UpdateActionBtnText
import com.example.database.domain.use_cases.action_button.UpdateAllActionButtonsPositionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimationViewModel
    @Inject constructor(

        private val allActionButtonsUseCase: AllActionButtonByNavigationIdUseCase,
        private val allFavouriteActionButtonUseCase: AllFavouriteActionButtonUseCase,
        private val updateSliderBtnText: UpdateActionBtnText,
        private val updateAllActionButtonsPositionUseCase: UpdateAllActionButtonsPositionUseCase,
        private val addToFavourite: AddToFavourite,
        private val deleteFavourite: DeleteFavourite,
        @ApplicationContext private val  context: Context,
        @IoDispatcher val ioDispatcher: CoroutineDispatcher,
        @MainDispatcher val mainDispatcher: CoroutineDispatcher,
    )
    : ViewModel() {

        init {

            //get action Buttons
            getActionButtons(1L)

        }

    var showShutDowndialog by mutableStateOf(false)


    private var job: Job? = null

    private var _showUpdateSliderBtnDialog = mutableStateOf(false)
    val showUpdateSliderBtnDialog: MutableState<Boolean> = _showUpdateSliderBtnDialog

    private val _navigationItemWithButtons = MutableStateFlow(DynamicScreenState(true))
    val navigationItemWithButtons: StateFlow<DynamicScreenState> = _navigationItemWithButtons

    // State to hold the clicked status of buttons
    private val _buttonStates = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val buttonStates: StateFlow<Map<String, Boolean>> = _buttonStates

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

    fun toggleButtonState(action: String , isChecked: Boolean) {
        Log.d("TAG","topic  = $action")
        _buttonStates.value += (action to isChecked)
    }

    fun resetAllButtonStates() {
        _buttonStates.value = _buttonStates.value.mapValues { false }
    }


    fun UpdateSliderBtnState(value: Boolean){
        _showUpdateSliderBtnDialog.value = value

    }

    fun addFavouriteActionBtn(actionButton: ActionButtons){

        viewModelScope.launch(ioDispatcher) {

            val btn = actionButton.copy(
                isFavorite = true
            )

            addToFavourite(btn)

            getActionButtons(1L)
        }
    }

    fun deleteFavouriteActionBtn(actionButton: ActionButtons){

        viewModelScope.launch(ioDispatcher) {

            val btn = actionButton.copy(
                isFavorite = false
            )

            deleteFavourite(btn)

            getActionButtons(1L)
        }

    }


    fun saveActionButtonOrder() {


        // 1. Take a snapshot of the current list order from the UI state
        val currentOrderedList = _navigationItemWithButtons.value.navigationItemActionButtons

        viewModelScope.launch(ioDispatcher) {
            // 2. Prepare the items with their new positions in memory
            val updatedPositionsList = currentOrderedList.mapIndexed { index, item ->
                item.copy(position = index)
            }

            // 3. Call your UseCase/DAO with the entire list at once
            // This executes as a single transaction in Room!
            updateAllActionButtonsPositionUseCase(updatedPositionsList)

        }
    }


    fun moveActionButton(
        fromIndex: Int,
        toIndex: Int
    ) {

        val current =
            _navigationItemWithButtons.value
                .navigationItemActionButtons
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
                navigationItemActionButtons = current
            )
    }

}