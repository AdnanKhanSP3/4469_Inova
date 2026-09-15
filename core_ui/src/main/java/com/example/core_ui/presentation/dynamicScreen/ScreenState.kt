package com.example.core_ui.admin.presentation.dynamicScreen

import com.example.database.data.model.ActionButtons


data class ScreenState(

    val isLoading: Boolean = false,
    val navigationItemActionButtons: List<ActionButtons> = emptyList()

)
