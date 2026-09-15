package com.example.core_ui.preferenceDataStore.domain.use_cases

import com.example.core_ui.preferenceDataStore.domain.repositoy.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShowCaseUseCase
@Inject constructor(
    private val settingsRepository: SettingsRepository

){

    operator fun invoke(): Flow<Boolean> {
        return settingsRepository.getShowCase()
    }
}