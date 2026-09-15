package com.example.database.domain.use_cases.action_button

import com.example.database.data.model.ActionButtons
import com.example.database.data.model.SliderButton
import com.example.database.domain.repository.ActionButtonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AllFavouriteActionButtonUseCase
@Inject constructor(
    private    val  actionButtonRepository: ActionButtonRepository
){

    operator fun invoke(): Flow<List<ActionButtons>> {
        return actionButtonRepository.getAllFavouriteSliderButton()
    }
}