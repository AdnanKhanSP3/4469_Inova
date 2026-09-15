package com.example.database.domain.use_cases.navigation_item


import com.example.database.data.model.NavigationItem
import com.example.database.domain.repository.NavigationItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNavigationItemUseCase
@Inject constructor(
    private val repository: NavigationItemRepository
){

     operator fun invoke() : Flow<List<NavigationItem>> {

         return repository.getAllNavigationItems()
    }
}