package com.example.database.domain.repository

import com.example.database.data.model.SliderButton
import kotlinx.coroutines.flow.Flow

interface SliderButtonRepository {
    suspend fun insertSliderButton(sliderButton: SliderButton)
    suspend fun deleteSliderButton(sliderButton: SliderButton)
    fun getAllSliderButton(navigationItemId : Long): Flow<List<SliderButton>>
    fun updateFavourite(sliderButton: SliderButton)
    suspend fun updateAllSliderButtonsPosition(sliderButtons: List<SliderButton>)
    fun updateSliderButton(sliderButton: SliderButton)
    fun getAllFavouriteSliderButton(): Flow<List<SliderButton>>

    //New function for favourite status
    fun updateFavouriteStatus(buttonId: Long, isFav: Boolean)
}