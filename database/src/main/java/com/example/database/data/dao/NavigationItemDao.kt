package com.example.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.database.data.model.NavigationItem
import com.example.database.data.model.NavigationItemWithActionButtons
import com.example.database.data.model.NavigationItemWithSlider
import com.example.database.data.model.NavigationItemWithSliderButtons
import kotlinx.coroutines.flow.Flow

@Dao
interface NavigationItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNavigationItem(navigationItem: NavigationItem)

    @Delete
    suspend fun deleteNavigationItem(navigationItem: NavigationItem)

    @Query("SELECT * FROM NavigationItem ORDER BY position")
    fun getAllNavigationItems(): Flow<List<NavigationItem>>

    @Transaction
    @Query("SELECT * FROM NavigationItem ORDER BY position")
    fun getNavigationItemsWithActionButtons(): List<NavigationItemWithActionButtons>


    @Transaction
    @Query("SELECT * FROM NavigationItem ORDER BY position")
    fun getNavigationItemsWithSliderButtons(): List<NavigationItemWithSliderButtons>

    @Transaction
    @Query("SELECT * FROM NavigationItem")
    fun getNavigationItemsWithSlider(): List<NavigationItemWithSlider>

    @Update
    suspend fun updateNavigationItem(
        navigationItem: NavigationItem
    )
}
