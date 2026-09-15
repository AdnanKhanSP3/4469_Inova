package com.example.database.domain.use_cases.navigation_item

import com.example.database.data.model.NavigationItemWithSliderButtons
import com.example.database.domain.repository.NavigationItemRepository
import javax.inject.Inject

class NavigationItemWithSliderButtonUseCase
    @Inject constructor(
    private val navigationItemRepository: NavigationItemRepository
) {

    suspend  operator fun invoke(): List<NavigationItemWithSliderButtons>{
        return navigationItemRepository.getNavigationItemsWithSliderButtons()
    }
}