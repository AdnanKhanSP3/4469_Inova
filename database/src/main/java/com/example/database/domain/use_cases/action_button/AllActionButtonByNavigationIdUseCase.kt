package com.example.database.domain.use_cases.action_button


import com.example.database.data.model.ActionButtons
import com.example.database.domain.repository.ActionButtonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AllActionButtonByNavigationIdUseCase
@Inject constructor(
    private val     actionButtonRepository: ActionButtonRepository
){
    operator fun invoke(navigationId: Long) : Flow<List<ActionButtons>> {
        return actionButtonRepository.getAllActionButtons(navigationId)
    }
}