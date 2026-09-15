package com.example.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.database.data.model.SliderButton
import kotlinx.coroutines.flow.Flow

@Dao
interface SliderButtonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSliderButton(sliderButton: SliderButton)

    @Delete
    suspend fun deleteSliderButton(sliderButton: SliderButton)

    @Query("SELECT * FROM SliderButton WHERE navigationItemId = :navigationItemId ORDER BY position")
    fun getAllSliderButton(navigationItemId : Long): Flow<List<SliderButton>>

    @Update
    suspend fun updateAllSliderButtonsPosition(sliderButtons: List<SliderButton>)

    @Update
    fun updateFavouriteStatus(sliderButton: SliderButton)

    @Query("Update SliderButton SET isFavorite = :isFav  WHERE sliderButtonId = :buttonId")
    fun updateFavouriteStatus(buttonId: Long, isFav: Boolean)


    @Query("SELECT * FROM SliderButton WHERE isFavorite = 1")
    fun getAllFavouriteSliderButton(): Flow<List<SliderButton>>
}