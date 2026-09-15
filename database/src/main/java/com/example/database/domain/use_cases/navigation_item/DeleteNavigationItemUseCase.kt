package com.example.database.domain.use_cases.navigation_item


import com.example.database.data.model.NavigationItem
import com.example.database.domain.repository.NavigationItemRepository
import javax.inject.Inject

class DeleteNavigationItemUseCase
@Inject constructor(
   private val navigationItemRepository: NavigationItemRepository
){

    suspend operator fun invoke(navigationItem: NavigationItem) {
        navigationItemRepository.deleteNavigationItem(navigationItem)
    }
}