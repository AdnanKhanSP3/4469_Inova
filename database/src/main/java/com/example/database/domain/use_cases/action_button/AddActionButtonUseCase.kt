package com.example.database.domain.use_cases.action_button

import com.example.database.data.model.ActionButtons
import com.example.database.domain.repository.ActionButtonRepository
import javax.inject.Inject

class AddActionButtonUseCase
@Inject constructor(
    private val actionButtonRepository: ActionButtonRepository
) {

    suspend  operator fun invoke(actionButton: ActionButtons) {
        actionButtonRepository.insertActionButton(actionButton)
    }
}