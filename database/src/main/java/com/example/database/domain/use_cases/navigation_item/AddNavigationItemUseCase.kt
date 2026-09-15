package com.example.database.domain.use_cases.navigation_item


import com.example.database.data.model.NavigationItem
import com.example.database.domain.repository.NavigationItemRepository
import javax.inject.Inject

class AddNavigationItemUseCase
    @Inject constructor(
    private val repository: NavigationItemRepository
) {

    suspend  operator fun invoke(navigationItem: NavigationItem) {
        repository.insertNavigationItem(navigationItem)
    }
}
