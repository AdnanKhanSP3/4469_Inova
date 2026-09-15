package com.example.database.domain.use_cases.slider_button

import com.example.database.data.model.SliderButton
import com.example.database.domain.repository.SliderButtonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSliderButtonUseCase
@Inject constructor(
    private val sliderButtonRepository: SliderButtonRepository
){
     operator fun invoke(navigationId: Long) : Flow<List<SliderButton>> {
         return sliderButtonRepository.getAllSliderButton(navigationId)
    }
}