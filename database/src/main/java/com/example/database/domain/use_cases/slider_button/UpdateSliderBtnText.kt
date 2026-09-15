package com.example.database.domain.use_cases.slider_button

import com.example.database.data.model.SliderButton
import com.example.database.domain.repository.SliderButtonRepository
import javax.inject.Inject

class UpdateSliderBtnText
@Inject constructor(
    private    val  sliderButtonRepository: SliderButtonRepository
) {

    operator fun invoke(sliderButton: SliderButton) {
        sliderButtonRepository.updateFavourite(sliderButton)
    }
}