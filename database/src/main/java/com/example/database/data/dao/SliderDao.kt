package com.example.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.data.model.Sliders


@Dao
interface SliderDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSlider(slider: Sliders)

    @Query("SELECT * FROM Sliders WHERE navigationItemId = :navigationItemId")
    fun getAllSliders(navigationItemId: Long): Sliders

    @Delete
    suspend fun deleteSlider(slider: Sliders)

}