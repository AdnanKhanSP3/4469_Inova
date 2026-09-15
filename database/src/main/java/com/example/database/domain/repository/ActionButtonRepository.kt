package com.example.database.domain.repository

import com.example.database.data.model.ActionButtons
import com.example.database.data.model.SliderButton
import kotlinx.coroutines.flow.Flow

interface ActionButtonRepository {


    suspend fun insertActionButton(actionButton: ActionButtons)

    suspend fun deleteActionButton(actionButton: ActionButtons)

    fun getAllActionButtons(navigationItemId: Long): Flow<List<ActionButtons>>

    fun updateFavourite(actionButton: ActionButtons)

    suspend fun updateAllActionButtonsPosition(actionButtons: List<ActionButtons>)

    fun updateActionButton(actionButton: ActionButtons)

    fun getAllFavouriteSliderButton(): Flow<List<ActionButtons>>

    //New function for favourite status
    fun updateFavouriteStatus(buttonId: Long, isFav: Boolean)

}