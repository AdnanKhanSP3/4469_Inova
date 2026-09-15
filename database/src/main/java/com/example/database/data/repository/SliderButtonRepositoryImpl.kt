package com.example.database.data.repository

import com.example.database.data.dao.SliderButtonDao
import com.example.database.data.model.SliderButton
import com.example.database.domain.repository.SliderButtonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SliderButtonRepositoryImpl
@Inject constructor(
    private val sliderButtonDao: SliderButtonDao
): SliderButtonRepository {
    override suspend fun insertSliderButton(sliderButton: SliderButton) {
        sliderButtonDao.insertSliderButton(sliderButton)
    }

    override suspend fun deleteSliderButton(sliderButton: SliderButton) {

        sliderButtonDao.deleteSliderButton(sliderButton)
    }

    override fun getAllSliderButton(navigationItemId: Long): Flow<List<SliderButton>> {
        return sliderButtonDao.getAllSliderButton(navigationItemId)
    }

    override fun updateFavourite(sliderButton: SliderButton) {
        sliderButtonDao.updateFavouriteStatus(sliderButton)
    }

    override suspend fun updateAllSliderButtonsPosition(sliderButtons: List<SliderButton>) {
       sliderButtonDao.updateAllSliderButtonsPosition(sliderButtons)
    }

    override fun updateSliderButton(sliderButton: SliderButton) {
        sliderButtonDao.updateFavouriteStatus(sliderButton)
    }

    override fun getAllFavouriteSliderButton(): Flow<List<SliderButton>> {
        return  sliderButtonDao.getAllFavouriteSliderButton()
    }

    //new function for testing favrt status
    override fun updateFavouriteStatus(buttonId: Long, isFav: Boolean) {
        sliderButtonDao.updateFavouriteStatus(buttonId, isFav)
    }
}