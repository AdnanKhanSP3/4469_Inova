package com.example.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.database.data.model.ActionButtons
import com.example.database.data.model.SliderButton
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionButtonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActionButtons(actionButtons: ActionButtons)

    @Delete
    suspend fun deleteActionButtons(actionButtons: ActionButtons)

    @Query("SELECT * FROM ActionButtons WHERE navigationItemId = :navigationItemId ORDER BY position")
    fun getAllActionButtons(navigationItemId : Long): Flow<List<ActionButtons>>

    @Update
    suspend fun updateAllActionButtonsPosition(actionButtons: List<ActionButtons>)

    @Update
    fun updateFavouriteStatus(actionButtons: ActionButtons)

    @Query("Update ActionButtons SET isFavorite = :isFav  WHERE actionButtonId = :buttonId")
    fun updateFavouriteStatus(buttonId: Long, isFav: Boolean)

    @Query("SELECT * FROM ActionButtons WHERE isFavorite = 1")
    fun getAllFavouriteActionButton(): Flow<List<ActionButtons>>

}