package com.example.core_ui.preferenceDataStore.domain.use_cases

import com.example.core_ui.preferenceDataStore.domain.repositoy.SettingsRepository
import javax.inject.Inject

class SaveShowCaseUseCase
@Inject constructor(
    private val settingsRepository: SettingsRepository
){

    operator suspend fun invoke(value: Boolean){
        settingsRepository.saveShowCase(value)
    }
}