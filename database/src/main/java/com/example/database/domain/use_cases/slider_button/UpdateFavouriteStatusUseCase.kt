package com.example.database.domain.use_cases.slider_button

import com.example.database.domain.repository.SliderButtonRepository
import javax.inject.Inject


class UpdateFavouriteStatusUseCase
@Inject constructor(
    private val sliderButtonRepository: SliderButtonRepository
){
     operator fun invoke(buttonId: Long, isFav: Boolean) {
        sliderButtonRepository.updateFavouriteStatus(buttonId, isFav)
    }
}