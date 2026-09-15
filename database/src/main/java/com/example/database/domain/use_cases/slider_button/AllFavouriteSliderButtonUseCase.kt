package com.example.database.domain.use_cases.slider_button

import com.example.database.data.model.SliderButton
import com.example.database.domain.repository.SliderButtonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AllFavouriteSliderButtonUseCase
@Inject constructor(
    private val  sliderButtonRepository: SliderButtonRepository
){
    operator fun invoke(): Flow<List<SliderButton>> {
        return sliderButtonRepository.getAllFavouriteSliderButton()
    }
}