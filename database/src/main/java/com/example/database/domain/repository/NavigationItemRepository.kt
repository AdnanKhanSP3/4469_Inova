package com.example.database.domain.repository

import com.example.database.data.model.NavigationItem
import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.data.model.NavigationItemWithSlider
import com.example.database.data.model.NavigationItemWithSliderButtons
import kotlinx.coroutines.flow.Flow

interface NavigationItemRepository {

    suspend fun insertNavigationItem(navigationItem: NavigationItem)

    fun getAllNavigationItems(): Flow<List<NavigationItem>>

    suspend fun  getNavigationItemsWithActionButtons(): List<NavigationItemWithActionButtons>

    suspend fun  getNavigationItemsWithSliderButtons(): List<NavigationItemWithSliderButtons>

    suspend fun getNavigationItemWithSlider(): List<NavigationItemWithSlider>

    suspend fun deleteNavigationItem(navigationItem: NavigationItem)

    suspend fun updateNavigationItem(
        navigationItem: NavigationItem
    )

}