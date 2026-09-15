package com.example.core_ui.dynamicscreen.presentation

import com.example.database.data.model.ActionButtons
import com.example.database.data.model.SliderButton
import com.example.database.data.model.Sliders

data class DynamicScreenState(
    val isLoading: Boolean = false,
    val navigationItemActionButtons: List<ActionButtons> = emptyList(),
    val navigationItemSlider: Sliders? = null,
    val navigationItemSliderButton : List<SliderButton> = emptyList()
)