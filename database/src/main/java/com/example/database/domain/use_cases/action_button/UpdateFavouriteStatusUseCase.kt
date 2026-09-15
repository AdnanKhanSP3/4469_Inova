package com.example.database.domain.use_cases.action_button

import com.example.database.domain.repository.ActionButtonRepository
import com.example.database.domain.repository.SliderButtonRepository
import javax.inject.Inject


class UpdateFavouriteStatusUseCase
@Inject constructor(
    private val actionButtonRepository: ActionButtonRepository
){
     operator fun invoke(buttonId: Long, isFav: Boolean) {
         actionButtonRepository.updateFavouriteStatus(buttonId, isFav)
    }
}