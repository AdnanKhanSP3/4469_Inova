package com.example.database.data.repository


import com.example.database.data.dao.NavigationItemDao
import com.example.database.data.model.NavigationItem
import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.data.model.NavigationItemWithSlider
import com.example.database.data.model.NavigationItemWithSliderButtons
import com.example.database.domain.repository.NavigationItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NavigationItemRepositoryImpl
    @Inject constructor(
        private val navigationItemDao: NavigationItemDao
    ): NavigationItemRepository {

    override suspend fun insertNavigationItem(navigationItem: NavigationItem) {
        navigationItemDao.insertNavigationItem(navigationItem)
    }

    override fun getAllNavigationItems(): Flow<List<NavigationItem>> {

        return navigationItemDao.getAllNavigationItems()
    }

   suspend  override fun getNavigationItemsWithActionButtons(): List<NavigationItemWithActionButtons> {
        return navigationItemDao.getNavigationItemsWithActionButtons()
    }

    override suspend fun getNavigationItemsWithSliderButtons(): List<NavigationItemWithSliderButtons> {
        return navigationItemDao.getNavigationItemsWithSliderButtons()
    }

    override suspend fun getNavigationItemWithSlider(): List<NavigationItemWithSlider> {
        return navigationItemDao.getNavigationItemsWithSlider()
    }

    override suspend fun deleteNavigationItem(navigationItem: NavigationItem) {
        navigationItemDao.deleteNavigationItem(navigationItem)
    }

    override suspend fun updateNavigationItem(navigationItem: NavigationItem) {
        navigationItemDao.updateNavigationItem(navigationItem)
    }
}