package com.example.core_ui.admin.presentation.dynamicScreen

import com.example.database.data.model.ActionButtons
import com.example.database.data.model.SliderButton
import com.example.database.data.model.Sliders


sealed class ScreenEvent {

    class onAddEvent(val navigationItemId : Long):ScreenEvent()
    class onDeleteEvent(val actionButtons: ActionButtons):ScreenEvent()
    class onGetButtonsEvent(val actionButtons: ActionButtons):ScreenEvent()
    class onAddSliderEvent(val navigationItemId: Long):ScreenEvent()
    class onDeleteSliderEvent(val slider: Sliders): ScreenEvent()
    class onAddSliderButtonEvent(val navigationItemId: Long):ScreenEvent()
    class onDeleteSliderButtonEvent(val sliderButton: SliderButton):ScreenEvent()
}