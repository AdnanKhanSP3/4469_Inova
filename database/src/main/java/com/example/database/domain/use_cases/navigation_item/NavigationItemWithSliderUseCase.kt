package com.example.database.domain.use_cases.navigation_item

import com.example.database.data.model.NavigationItemWithSlider
import com.example.database.domain.repository.NavigationItemRepository
import javax.inject.Inject

class NavigationItemWithSliderUseCase
@Inject constructor(
    private val navigationItemRepository: NavigationItemRepository
){

    suspend  operator  fun  invoke (): List<NavigationItemWithSlider>{
        return navigationItemRepository.getNavigationItemWithSlider()
    }
}