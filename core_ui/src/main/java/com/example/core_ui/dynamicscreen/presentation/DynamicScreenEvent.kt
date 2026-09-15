package com.example.core_ui.dynamicscreen.presentation

import com.example.database.data.model.ActionButtons
import com.example.database.data.model.SliderButton
import com.example.database.data.model.Sliders


sealed class DynamicScreenEvent {

    class onAddEvent(val navigationItemId : Long):DynamicScreenEvent()
    class onDeleteEvent(val actionButtons: ActionButtons):DynamicScreenEvent()
    class onGetButtonsEvent(val actionButtons: ActionButtons):DynamicScreenEvent()
    class onAddSliderEvent(val navigationItemId: Long):DynamicScreenEvent()
    class onDeleteSliderEvent(val slider: Sliders):DynamicScreenEvent()
    class onAddSliderButtonEvent(val navigationItemId: Long):DynamicScreenEvent()
    class onDeleteSliderButtonEvent(val sliderButton: SliderButton):DynamicScreenEvent()
}