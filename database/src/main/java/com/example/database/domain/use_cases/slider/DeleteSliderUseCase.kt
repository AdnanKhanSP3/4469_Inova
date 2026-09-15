package com.example.database.domain.use_cases.slider

import com.example.database.data.model.Sliders
import com.example.database.domain.repository.SliderRepository

import javax.inject.Inject

class DeleteSliderUseCase
@Inject constructor(
    private val sliderRepository: SliderRepository
){

    operator  suspend fun invoke(slider : Sliders){
        sliderRepository.deleteSlider(slider)

    }
}