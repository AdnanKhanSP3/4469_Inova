package com.example.core_ui.main.presentation

import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.data.model.NavigationItemWithSlider


data class MainScreenState(
    val isLoading: Boolean = true,
    val navigationItems: List<NavigationItemWithActionButtons> = emptyList(),
    val error: String? = null,
    val sliderItem: List<NavigationItemWithSlider> = emptyList()
)
