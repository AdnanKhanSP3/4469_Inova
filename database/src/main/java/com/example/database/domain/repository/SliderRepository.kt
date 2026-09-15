package com.example.database.domain.repository

import com.example.database.data.model.Sliders

interface SliderRepository {

    suspend fun insertSlider(slider: Sliders)

    suspend fun deleteSlider(slider: Sliders)

    suspend fun getAllSliders(navigationItemId: Long): Sliders
}