package com.example.database.domain.use_cases.slider

import com.example.database.data.model.Sliders
import com.example.database.domain.repository.SliderRepository
import javax.inject.Inject


class AllSlidersByNavigationItem
@Inject constructor(
    private val sliderRepository: SliderRepository
){

   suspend  operator fun invoke(navigationId: Long) : Sliders {
        return sliderRepository.getAllSliders(navigationId)
    }

}