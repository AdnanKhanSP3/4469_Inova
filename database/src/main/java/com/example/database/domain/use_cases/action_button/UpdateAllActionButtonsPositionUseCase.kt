package com.example.database.domain.use_cases.action_button

import com.example.database.data.model.ActionButtons
import com.example.database.data.model.SliderButton
import com.example.database.domain.repository.ActionButtonRepository
import com.example.database.domain.repository.SliderButtonRepository
import javax.inject.Inject


class UpdateAllActionButtonsPositionUseCase
@Inject constructor(
    private val actionButtonRepository: ActionButtonRepository
){

     suspend operator fun invoke(actionButtons: List<ActionButtons>){
         actionButtonRepository.updateAllActionButtonsPosition(actionButtons)
    }
}