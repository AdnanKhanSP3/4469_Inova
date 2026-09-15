package com.example.database.domain.use_cases.navigation_item


import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.domain.repository.NavigationItemRepository
import javax.inject.Inject


class NavigationItemsWithActionButtonsUseCase
@Inject constructor(
    private val navigationItemRepository: NavigationItemRepository
) {

   suspend  operator fun invoke(): List<NavigationItemWithActionButtons> {
        return navigationItemRepository.getNavigationItemsWithActionButtons()
    }
}